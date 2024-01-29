package com.keyin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {

    private SuggestionEngine suggestionEngine;

    @Mock
    private SuggestionsDatabase mockSuggestionDB;

    @BeforeEach
    public void setUp() throws Exception {
        suggestionEngine = new SuggestionEngine();
        URL resource = getClass().getClassLoader().getResource("words.txt");
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            Path path = Paths.get(resource.toURI());
            suggestionEngine.loadDictionaryData(path);
        }
    }



    @Test
    public void testValidInputWithNoSuggestion() {
        assertEquals("", suggestionEngine.generateSuggestions("pneumonoultramicroscopicsilicovolcanoconiosis"));
    }

    @Test
    public void testEmptyInput() {
        assertEquals("", suggestionEngine.generateSuggestions(""));
    }

    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> suggestionEngine.generateSuggestions(null));
    }

    @Test
    public void testInputWithSpecialCharacters() {
        assertEquals("", suggestionEngine.generateSuggestions("hello!"));
    }

    @Test
    public void testInputWithNumbers() {
        assertEquals("", suggestionEngine.generateSuggestions("h3llo"));
    }

    @Test
    public void testLargeInputString() {
        String largeInput = "thisisaverylonginputstringtotesttheenginesbehaviorwithlonginputs";
        assertEquals("", suggestionEngine.generateSuggestions(largeInput));
    }

    @Test
    public void testLoadingOfDictionaryData() {
        assertNotNull(suggestionEngine.getWordSuggestionDB());
        assertFalse(suggestionEngine.getWordSuggestionDB().isEmpty());
    }

    @Test
    public void testCaseInsensitivity() {
        assertEquals(suggestionEngine.generateSuggestions("HELLO"), suggestionEngine.generateSuggestions("hello"));
    }

    @Test
    public void testEngineWithMockDatabase() {
        Map<String, Integer> mockData = new HashMap<>();
        mockData.put("test", 1);
        Mockito.when(mockSuggestionDB.getWordMap()).thenReturn(mockData);

        suggestionEngine.setWordSuggestionDB(mockSuggestionDB);

        assertTrue(suggestionEngine.generateSuggestions("tes").contains("test"));
    }
}