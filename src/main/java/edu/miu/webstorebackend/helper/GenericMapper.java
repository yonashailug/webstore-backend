package edu.miu.webstorebackend.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericMapper<T, E> {
    ModelMapper modelMapper;

    @Autowired
    public GenericMapper(ModelMapper mapper) {
        this.modelMapper = mapper;
    }

    /**
     * @param list the list of items you want to convert
     * @param convertTo the data type you want your list to be converted
     */
    public  List<E> mapList(List<T> list, Class<E> convertTo) {
        return list.stream()
                .map( item -> modelMapper.map(item, convertTo))
                .collect(Collectors.toList());

    }
    /**
     * @param source the source class you want to map
     * @param target the target class your source needs to be mapped to
     */
    public  E mapObject(T source , Class<E> target) {
        return modelMapper.map(source, target);
    }
}
