package nl.han.ica.datastructures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HANQueueTest {
    private IHANQueue<Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new HANQueue<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void enqueueAddsElementToBackOfQueue() {
        // Arrange
        int frontElement = 1;
        int backElement = 2;

        // Act
        this.sut.enqueue(frontElement);
        this.sut.enqueue(backElement);

        // Assert
        assertEquals(frontElement, this.sut.peek());
    }

    @Test
    void dequeueRemovesElementFirstInQueue() {
        // Arrange
        int frontElement = 1;
        int backElement = 2;

        // Act
        this.sut.enqueue(frontElement);
        this.sut.enqueue(backElement);

        // Assert
        assertEquals(frontElement, this.sut.dequeue());
        assertEquals(backElement, this.sut.peek());
    }

    @Test
    void clearDeletesEntireList() {
        // Arrange
        Integer expectedLengthBeforeClear = 1;
        Integer expectedLength = 0;
        sut.enqueue(1);

        // Act
        assertEquals(1, sut.getSize());
        sut.clear();

        // Assert
        assertEquals(expectedLength, sut.getSize());
    }

    @Test
    void isEmpty() {
        // Arrange
        sut.enqueue(1);

        // Act
        assertFalse(sut.isEmpty());
        sut.clear();

        // Assert
        assertTrue(sut.isEmpty());
    }
}