package zci.trainning.userservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import zci.trainning.userservice.dto.DepartmentDto;
import zci.trainning.userservice.dto.ResponseDto;
import zci.trainning.userservice.dto.UserDto;
import zci.trainning.userservice.entity.User;
import zci.trainning.userservice.repository.UserRepository;
import zci.trainning.userservice.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WebClient webClient;


    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseDto getUser(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        User user = userRepository.findById(userId).get();
        UserDto userDto = mapToUser(user);

        DepartmentDto departmentDto = webClient.get().uri("http://localhost:8080/api/departments/" + user.getDepartmentId())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        responseDto.setUser(userDto);
        responseDto.setDepartment(departmentDto);

        return responseDto;
    }

    private UserDto mapToUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
