package com.keyin;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine = new SuggestionEngine();

    @Mock
    private SuggestionsDatabase mockSuggestionDB;
    private boolean testInstanceSame = false;


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