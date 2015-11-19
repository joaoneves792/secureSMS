package pt.ulisboa.tecnico.meic.sirs.group6.securesms.dataAccess;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by lribeirogomes on 18/11/15.
 */
public class SandboxTest extends TestCase {

    private String _password, _data;
    private long _startTime, _endTime;

    @Before
    public void setUp() {
        _password = "Ildefonsina";
        _data = "Hello World";
        _startTime = new Date().getTime();
    }

    @After
    public void tearDown() {
        _endTime = new Date().getTime();
        long difference = _endTime - _startTime;
        System.out.println("Elapsed milliseconds: " + difference);
    }

    @Test
    public void testSuccess() throws Exception {
        EncryptionService service = new EncryptDBDataService(_password, _data);
        service.Execute();

        byte[] encryptedData = service.getResult();

        DecryptDBDataService service2 = new DecryptDBDataService(_password, encryptedData);
        service2.Execute();

        String decryptedData = service2.getResult();

        assertEquals(_data, decryptedData);
    }
}