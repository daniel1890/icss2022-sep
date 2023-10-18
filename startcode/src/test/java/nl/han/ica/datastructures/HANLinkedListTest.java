package nl.han.ica.datastructures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HANLinkedListTest {
    private HANLinkedList<Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new HANLinkedList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addIntegerToFirstOfListReturnsFirstValueSuccesfully() {
        // Arrange
        Integer expected = 1;
        int expectedLength = 2;

        // Act
        sut.addFirst(2);
        sut.addFirst(expected);

        // Assert
        assertEquals(expected, sut.getFirst());
        assertEquals(expectedLength, sut.getSize());
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
}