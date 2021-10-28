package com.grin.lexicom.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.grin.lexicom.dto.UserDto;

import java.io.IOException;

public class DictionaryCreatorSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User u, JsonGenerator jg, SerializerProvider sp) throws IOException {
        UserDto userDto = UserDto.fromUser(u);
        jg.writeObject(userDto);
    }
}
