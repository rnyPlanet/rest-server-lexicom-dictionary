package com.grin.lexicom.dto;

import com.grin.lexicom.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserSubscribeDictionaryDto extends UserDto {

    public static List<UserSubscribeDictionaryDto> fromUser(Collection<User> us) {
        List<UserSubscribeDictionaryDto> list = new ArrayList();

        for (User user : us) {
            UserSubscribeDictionaryDto userDto = new UserSubscribeDictionaryDto();
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setSurName(user.getSurName());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            list.add(userDto);
        }

        return list;
    }
}
