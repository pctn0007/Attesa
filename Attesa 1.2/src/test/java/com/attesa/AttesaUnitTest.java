package com.attesa;

import org.junit.Before;
import org.junit.Test;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import static junit.framework.Assert.assertEquals;



public class AttesaUnitTest {

    private Clinic clinic;
    private Visit visit;
    private static final String FAKE_CRED_STRING = "Identifiants incorrects!";
    private Context context = ApplicationProvider.getApplicationContext();

    @Before
    public void setUp() throws Exception{
        clinic = new Clinic("01","ClinicTest","TestType","TestCphone","TestClive",1);
        visit = new Visit("TestCNAME","TestUHC","TestVote");
    }

    @Test
    public void testClinic() throws Exception{
        assertEquals(true,clinic.checkCid(clinic.cid));

    }

    @Test
    public void testVisit() throws Exception{
        assertEquals(true,visit.checkCname(visit.cname));

    }

    @Test
    public void testFRText() throws Exception{
    LoginActivity myObjectUnderTest = new LoginActivity();

    String result = myObjectUnderTest.getString(R.string.str_incorrectCreds);

    // ...then the result should be the expected one.
    assertEquals(true,FAKE_CRED_STRING.equals(result));
}

}