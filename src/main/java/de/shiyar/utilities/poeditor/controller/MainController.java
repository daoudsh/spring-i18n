package de.shiyar.utilities.poeditor.controller;

import de.shiyar.utilities.poeditor.PoeditorApplication;
import de.shiyar.utilities.poeditor.db.entities.MessagesEntity;
import de.shiyar.utilities.poeditor.db.repositories.MessagesRepository;
import de.shiyar.utilities.poeditor.pojo.POEditorJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author daoud
 * on Aug, 2019
 */
@Controller
@Slf4j
public class MainController {

    @Value("${log.directory.path}")
    private String logFolder;

    @Value("${spring.database.name}")
    private String dbName;

    private ObjectMapper objectMapper;
    private MessagesRepository messagesRepository;

    public MainController(ObjectMapper objectMapper, MessagesRepository messagesRepository) {
        this.objectMapper = objectMapper;
        this.messagesRepository = messagesRepository;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        return "uploadForm";
    }

    @PostMapping(path = "poeditor/download", consumes = {"multipart/form-data"})
    public void getSqlScript(@RequestParam MultipartFile file, @RequestParam String language, HttpServletResponse response) {

        log.info("language : " + language);
        String originalFilename = file.getOriginalFilename();
        String downloadFileName = (originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : "sql") + ".sql";
        String userHome = System.getProperty("user.home");
        String outputFile = userHome + File.separator + logFolder + File.separator + "insert.log";


        try (FileInputStream fis = new FileInputStream(outputFile)) {

            POEditorJson[] poEditorJsonArray = objectMapper.readValue(file.getInputStream(), POEditorJson[].class);
            List<POEditorJson> poEditorJsons = Arrays.asList(poEditorJsonArray);
            poEditorJsons.forEach(poEditorJson -> {
                MessagesEntity messagesEntity = MessagesEntity.builder().local(language).code(poEditorJson.getTerm()).message(poEditorJson.getDefinition()).build();
                messagesRepository.insertUpdate(messagesEntity.getCode(), messagesEntity.getLocal(), messagesEntity.getMessage());
            });

            String truncate = String.format("%n use %s; %n%n", dbName);

            String sql = IOUtils.toString(fis);


            response.setContentType("application/sql");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
            IOUtils.copy(IOUtils.toInputStream(truncate + sql), response.getOutputStream());
            response.flushBuffer();

        } catch (IOException e) {
            log.info("Error writing file to output stream. Filename was '{}'", file.getName(), e);
            throw new RuntimeException("IOError writing file to output stream");
        }
        PoeditorApplication.restart();
    }
}
