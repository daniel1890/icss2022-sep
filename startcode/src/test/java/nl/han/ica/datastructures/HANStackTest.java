package nl.han.ica.datastructures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HANStackTest {
    IHANStack<Integer> sut;

    @BeforeEach
    void setUp() {
        sut = new HANStack<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void pushElementOnTopOfStack() {
        // Arrange
        int element = 1;

        // Act
        sut.push(element);

        // Assert
        assertEquals(element, sut.peek());
    }

    @Test
    void popRemovesElementOnTopOfStack() {
        // Arrange
        int bottomElement = 1;
        int topElement = 2;
        sut.push(bottomElement);
        sut.push(topElement);

        // Act
        sut.pop();

        // Assert
        assertEquals(bottomElement, sut.peek());
    }

}