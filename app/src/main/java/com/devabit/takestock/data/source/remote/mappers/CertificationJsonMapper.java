package com.devabit.takestock.data.source.remote.mappers;

import com.devabit.takestock.data.models.Certification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Artemyev on 29/04/2016.
 */
public class CertificationJsonMapper implements JsonMapper<Certification> {

    public static final String ID = "pk";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LOGO = "logo";

    public List<Certification> fromJsonStringToList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int length = jsonArray.length();
        List<Certification> result = new ArrayList<>(length);
        for (int index = 0; index < length; index++) {
            String jsonString = jsonArray.getString(index);
            Certification certification = fromJsonString(jsonString);
            result.add(certification);
        }
        return result;
    }

    @Override public Certification fromJsonString(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int id = jsonObject.getInt(ID);
        String name = jsonObject.getString(NAME);
        String description = jsonObject.getString(DESCRIPTION);
        String logo = jsonObject.getString(LOGO);

        Certification certification = new Certification();
        certification.setId(id);
        certification.setName(name);
        certification.setDescription(description);
        certification.setLogoUrl(logo);

        return certification;
    }

    @Override public String toJsonString(Certification target) throws JSONException {
        throw new UnsupportedOperationException("This operation not required.");
    }
}
