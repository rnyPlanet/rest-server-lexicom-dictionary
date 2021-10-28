package com.grin.lexicom.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.grin.lexicom.dto.UserSubscribeDictionaryDto;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class UserSubscribeDictionarySerializer extends JsonSerializer<Collection<User>> {

    @Override
    public void serialize(Collection<User> u, JsonGenerator jg, SerializerProvider sp) throws IOException {
        List<UserSubscribeDictionaryDto> users = UserSubscribeDictionaryDto.fromUser(u);
        jg.writeObject(users);
    }

}
