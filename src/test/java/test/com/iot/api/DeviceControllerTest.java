package test.com.iot.api;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

import com.iot.api.controllers.DeviceController;
import com.iot.api.service.*;

import com.iot.api.resources.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DeviceController.class})
@WebMvcTest(controllers={DeviceController.class})
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class DeviceControllerTest {

	@MockBean
	DeviceServiceInterface deviceService;

	@MockBean
	DeviceDataServiceInterface deviceDataService;
	
	@Autowired
	private MockMvc mockMvc;	
	
	@Test
    public void deviceDataShouldReturnDefaultMessage() throws Exception {
		when(deviceService.getAllDevices()).thenReturn(null);
        this.mockMvc.perform(get("/device/data"))
            .andExpect(status().isOk())
            .andDo(document("deviceData"));
    }
	
	@Test
    public void deviceShouldReturnDefaultMessage() throws Exception {
		when(deviceDataService.getAllDevices()).thenReturn(null);
        this.mockMvc.perform(get("/device"))
            .andExpect(status().isOk())
            .andDo(document("device"));
    }
}
