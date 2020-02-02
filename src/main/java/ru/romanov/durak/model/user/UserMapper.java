package ru.romanov.durak.model.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.romanov.durak.model.user.dto.UserDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    @Mapping(target = "hasPhoto",expression  = "java(user.getPhoto() != null)")
    UserDto userToUserDto(User user);

}
