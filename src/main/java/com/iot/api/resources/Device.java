package com.iot.api.resources;

public class Device {

		private long id;
		private String name;
		
		public Device() {
			
		}
		
		public Device(long id, String name) {
			setId(id);
			setName(name);
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
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
