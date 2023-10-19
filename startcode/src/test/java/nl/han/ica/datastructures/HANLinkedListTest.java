package nl.han.ica.datastructures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HANLinkedListTest {
    private IHANLinkedList<Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new HANLinkedList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insertIntegerToFirstOfList() {
        // Arrange
        Integer expected = 1;
        int expectedLength = 3;

        // Act
        sut.addFirst(3);
        sut.addFirst(2);
        sut.addFirst(expected);

        // Assert
        assertEquals(expected, sut.getFirst());
        assertEquals(expectedLength, sut.getSize());
    }

    @Test
    void insertIntegerToMiddleOfList() {
        // Arrange
        Integer expected = 10;
        sut.insert(0, 1);
        sut.insert(1, 2);
        sut.insert(2, 3);

        // Act
        sut.insert(1, expected);

        // Assert
        assertEquals(1, sut.get(0));
        assertEquals(expected, sut.get(1));
        assertEquals(2, sut.get(2));
    }


    @Test
    void deleteFirstIntegerOfList() {
        // Arrange
        Integer expected = 1;
        int expectedLength = 1;

        // Act
        sut.addFirst(2);
        sut.addFirst(expected);
        sut.removeFirst();

        // Assert
        assertEquals(expected, sut.getFirst());
        assertEquals(expectedLength, sut.getSize());
    }

    @Test
    void clearDeletesEntireList() {
        // Arrange
        sut.addFirst(1);
        sut.addFirst(2);
        sut.addFirst(3);
        int expectedBeforeClear = 3;
        int expectedAfterClear = 0;

        // Act
        assertEquals(expectedBeforeClear, sut.getSize());
        sut.clear();

        // Assert
        assertEquals(expectedAfterClear, sut.getSize());
    }

    @Test
    void removeFirstIntegerOfList() {
        // Arrange
        Integer expected = 2;
        int expectedLengthBeforeRemove = 2;
        int expectedLengthAfterRemove = 1;
        sut.addFirst(expected);
        sut.addFirst(1);

        // Act
        assertEquals(expectedLengthBeforeRemove, sut.getSize());
        sut.removeFirst();

        // Assert
        assertEquals(expected, sut.getFirst());
        assertEquals(expectedLengthAfterRemove, sut.getSize());
    }

    @Test
    void deleteLastIntegerOfList() {
        // Arrange
        Integer expected = 2;
        sut.addFirst(3);
        sut.addFirst(expected);
        sut.addFirst(1);

        // Act
        sut.delete(2);

        // Assert
        assertThrows(NoSuchElementException.class, () -> sut.get(2));
        assertEquals(expected, sut.get(1));
    }

    @Test
    void deleteMiddleIntegerOfList() {
        // Arrange
        Integer expectedLength = 2;
        sut.addFirst(3);
        sut.addFirst(2);
        sut.addFirst(1);

        // Act
        sut.delete(1);

        // Assert
        assertEquals(1, sut.get(0));
        assertEquals(3, sut.get(1));
        assertEquals(expectedLength, sut.getSize());
    }

    @Test
    void iterator() {
        // Arrange
        List<Integer> expected = Arrays.asList(3, 2, 1);
        List<Integer> actual = new ArrayList<>();
        sut.addFirst(1);
        sut.addFirst(2);
        sut.addFirst(3);

        // Act
        for (Integer number : sut) {
            actual.add(number);
        }

        // Assert
        assertEquals(expected, actual);
    }
}