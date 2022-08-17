package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.eventsservice.dtos.UserDTO;
import sit.int221.eventsservice.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public List<UserDTO> getAllUser(){
        return this.listMapper.mapList(this.repository.findAll(Sort.by("name").descending()), UserDTO.class, this.modelMapper);
    }
}
