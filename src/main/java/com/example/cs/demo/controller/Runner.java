package com.example.cs.demo.controller;

import com.example.cs.demo.model.Task;
import com.example.cs.demo.processor.Processor;
import com.example.cs.demo.reader.Reader;
import com.example.cs.demo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Component
@Slf4j
public class Runner implements CommandLineRunner {

    private static final String filename = "logfile.txt";

    @Autowired
    private Reader<String> reader;

    @Autowired
    private Processor<Task, String> processor;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.print("Enter the path to " + filename + " : ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String filepath = br.readLine();
        Path path = Paths.get(filepath, filename);

        log.info("reading data from file", path);
        Stream<String> data = reader.readData(path);
        Map<String, Task> map = new HashMap<>();
        data.parallel().forEach(taskinfo -> {
            if (taskinfo.strip().length() > 0) {
                log.debug("trying to parse ", taskinfo, " as a Task");
                Task task = processor.process(taskinfo);

                Task taskFromMap = map.get(task.getId());
                if (Optional.ofNullable(taskFromMap).isEmpty()) {
                    map.put(task.getId(), task);
                } else {
                    if (Math.abs(taskFromMap.getTimestamp() - task.getTimestamp()) > 4)
                        task.setFlag(true);
                    else
                        task.setFlag(false);

                    log.debug("trying to save ", task);
                    taskRepository.save(task);
                    log.info("Saved task ", task);

                    log.debug("removing task from memory", taskFromMap, " current map size: " + map.size());
                    map.remove(taskFromMap.getId());
                }
            }
        });

    }
}
