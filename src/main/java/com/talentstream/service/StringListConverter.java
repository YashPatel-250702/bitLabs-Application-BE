//package com.talentstream.service;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
//
//@Converter(autoApply = true)
//public class StringListConverter implements AttributeConverter<List<String>, String> {
//
//	private static final ObjectMapper objectMapper = new ObjectMapper();
//
//	@Override
//	public String convertToDatabaseColumn(List<String> attribute) {
//		try {
//
//			return objectMapper.writeValueAsString(attribute);
//		} catch (Exception e) {
//			throw new RuntimeException("Error converting list to JSON", e);
//		}
//	}
//
//	@Override
//	public List<String> convertToEntityAttribute(String dbData) {
//		try {
//
//			return objectMapper.readValue(dbData, new TypeReference<List<String>>() {
//			});
//		} catch (Exception e) {
//			throw new RuntimeException("Error converting JSON to list", e);
//		}
//	}
//}
