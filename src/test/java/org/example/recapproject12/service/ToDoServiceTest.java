package org.example.recapproject12.service;

import org.example.recapproject12.dto.ToDoDTO;
import org.example.recapproject12.enums.Status;
import org.example.recapproject12.model.ToDo;
import org.example.recapproject12.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoServiceTest {

    private ToDoRepository mockRepo = mock(ToDoRepository.class);
    private IdService idServiceMock = mock(IdService.class);
    private ToDoService toDoService = new ToDoService(mockRepo, idServiceMock);

    @Test
    void getAll_shouldReturnListOfTwo_WhenInitializedWithTwoEntrys() {
        //Given
        ToDo toDo1 = new ToDo("1","This is a task", Status.OPEN);
        ToDo toDo2 = new ToDo("2", "This is a task", Status.OPEN);
        List<ToDo> expected = List.of(toDo1, toDo2);
        when(mockRepo.findAll()).thenReturn(expected);

        //When
        List<ToDo> actual = toDoService.getAll();

        //Then
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void getByID() {
        //Given
        ToDo expected = new ToDo("1","This is a task", Status.OPEN);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));

        //When
        ToDo actual = toDoService.getByID("1");

        //Then
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById("1");
    }

    @Test
    void addToDo() {
        //Given
        ToDoDTO newToDTO = new ToDoDTO("This is a task", Status.OPEN);
        ToDo expected = new ToDo("1","This is a task", Status.OPEN);
        when(idServiceMock.randomID()).thenReturn("1");
        when(mockRepo.save(expected)).thenReturn(expected);

        //When
        ToDo actual = toDoService.addToDo(newToDTO);

        //Then
        verify(mockRepo, times(1)).save(expected);
        assertEquals(actual, expected);
    }

    @Test
    void updateToDo() {
        //Given
        ToDoDTO updateWith = new ToDoDTO("This is another task", Status.DONE);
        ToDo toUpdate = new ToDo("1","This is a task", Status.OPEN);
        ToDo expected = new ToDo("1","This is another task", Status.DONE);
        when(mockRepo.findById("1")).thenReturn(Optional.of(toUpdate));
        when(mockRepo.save(expected)).thenReturn(expected);

        //When
        ToDo actual = toDoService.updateToDo("1", updateWith);

        //Then
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById("1");
        verify(mockRepo, times(1)).save(expected);


    }

    @Test
    void deleteById() {
        //Given
        ToDoDTO newToDo = new ToDoDTO("This is another task", Status.DONE);
        ToDo expected = new ToDo("1","This is a task", Status.OPEN);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));

        //When
        ToDo actual = toDoService.deleteById("1");

        //Then
        assertEquals(expected, actual);
        verify(mockRepo, times(1)).findById("1");
        verify(mockRepo, times(1)).deleteById("1");
    }
}