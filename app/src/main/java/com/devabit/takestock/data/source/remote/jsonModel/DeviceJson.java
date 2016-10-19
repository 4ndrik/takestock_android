package com.devabit.takestock.data.source.remote.jsonModel;

import com.devabit.takestock.data.model.Device;

/**
 * Created by Victor Artemyev on 19/10/2016.
 */

public class DeviceJson implements JsonModel {

    public static class Request {
        public String device_id;
        public String registration_id;
        public String type;
        public String name;
        public int active;

        public Request(Device device) {
            this.device_id = device.getId();
            this.registration_id = device.getRegistrationId();
            this.type = device.getType();
            this.name = device.getName();
            this.active = device.isActive() ? 1 : 0;
        }
    }

    public static class Response {
        public int id;
        public String device_id;
        public String registration_id;
        public String type;
        public String name;
        public boolean active;
        public String date_created;
    }
}
