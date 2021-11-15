package com.dark002.school_management.service;

import com.dark002.school_management.model.CourseStructure;
import com.dark002.school_management.model.Stream;
import com.dark002.school_management.model.Classroom;
import com.dark002.school_management.repository.CourseStructureRepository;
import com.dark002.school_management.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamService {
    @Autowired
    private StreamRepository streams;

    @Autowired
    private CourseStructureRepository courseStructures;

    public List<Stream> getAllStreams() {
        return streams.getAll();
    }

    public void createStream(Stream stream) {
        streams.createStream(stream);
    }

    public void deleteStream(Stream stream) {
        streams.deleteStream(stream.getId());
    }

    public Stream getStreamById(String id) {
        return streams.getById(Integer.parseInt(id));
    }

    public List<Stream> getAllStreamsWithCourseStructure(Classroom classroom) {
        List<Stream> streamList = streams.getAll();
        for (int i = 0; i < streamList.size(); i++) {
            try {
                CourseStructure structure = courseStructures.getByStreamAndClassroom(
                        streamList.get(i).getId(), classroom.getId());
                streamList.get(i).setCourseStructure(structure);
            } catch (Exception e) {}
        }
        return streamList;
    }
}
