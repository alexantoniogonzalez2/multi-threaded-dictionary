package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DictionaryServerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void errorInfo1() {
        String error1 = "Wrong Number of Parameters";
        DictionaryServer.errorInfo(error1);
        String realOutput1 = outContent.toString();
        String testOutput1 = "Error: " + error1 + ". It was expected at least two arguments, port number " +
                "(integer) and dictionary name (string).";
        Assertions.assertEquals(realOutput1, testOutput1);
    }

    @Test
    void errorInfo2() {
        String error2 = "Wrong Input Type for Port Number";
        DictionaryServer.errorInfo(error2);
        String realOutput2 = outContent.toString();
        String testOutput2 = "Error: " + error2 + ". It was expected an integer number for the port " +
                "argument.";
        Assertions.assertEquals(realOutput2, testOutput2);
    }

    @Test
    void errorInfo3() {
        String error3 = "Wrong Port Number";
        DictionaryServer.errorInfo(error3);
        String realOutput3 = outContent.toString();
        String testOutput3 = "Error: " + error3 + ". The number for the port number is not suitable. " +
                "Please try with other port number (ex.: 3005).";
        Assertions.assertEquals(realOutput3, testOutput3);
    }

    @Test
    void errorInfo4() {
        String error4 = "Port Already in Use";
        DictionaryServer.errorInfo(error4);
        String realOutput4 = outContent.toString();
        String testOutput4 = "Error: " + error4 + ". The port is already in use. Please try with " +
                "other number.";
        Assertions.assertEquals(realOutput4, testOutput4);
    }

    @Test
    void errorInfo5() {
        String error5 = "Client Closes Connection";
        DictionaryServer.errorInfo(error5);
        String realOutput5 = outContent.toString();
        String testOutput5 = error5 + ". The client 0 has closed the connection.";
        Assertions.assertEquals(realOutput5, testOutput5);
    }
}