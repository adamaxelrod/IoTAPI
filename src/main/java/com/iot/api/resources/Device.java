package com.iot.api.resources;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;


@Document(collection="devices")
public class Device {

		@Id Object id;
		private String name;
		
		public Device() {
			
		}
		
		public Device(Long id, String name) {
			setId(id);
			setName(name);
		}

		/**
		 * @return the id
		 */
		public Object getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		
		
}
