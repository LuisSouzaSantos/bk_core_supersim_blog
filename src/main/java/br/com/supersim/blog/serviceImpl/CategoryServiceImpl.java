package br.com.supersim.blog.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.supersim.blog.DTO.CategoryDTO;
import br.com.supersim.blog.entity.Category;
import br.com.supersim.blog.exception.CategoryException;
import br.com.supersim.blog.repository.CategoryRepository;
import br.com.supersim.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public CategoryDTO save(CategoryDTO categoryDTO) throws CategoryException {
		
		Category category = getCategoryByName(categoryDTO.getName());
		
		if(category != null) { throw new CategoryException("CATEGORY_ALREADY_EXISTS"); }
		
		Category newCategory = categoryRepository.save(new Category(categoryDTO));
		
		return new CategoryDTO(newCategory);
	}

	@Override
	public void delete(Long categoryId) throws CategoryException {
		
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		
		if(categoryOptional.isPresent() == false) { throw new CategoryException("CATEGORY_NOT_FOUND"); } 
		
		categoryRepository.delete(categoryOptional.get());;
	}

	@Override
	public CategoryDTO update(CategoryDTO categoryDTO) throws CategoryException {

		if(categoryDTO.getId() == null) { throw new CategoryException("INVALID_CATEGORY"); }
		
		Category category = getCategoryByName(categoryDTO.getName());
		
		if(category != null && category.getName().equals(categoryDTO.getName()) && category.getId().equals(categoryDTO.getId()) == false) {
			throw new CategoryException("CATEGORY_ALREADY_EXISTS");
		}
		
		Category updatedCategory = categoryRepository.save(new Category(categoryDTO));
		
		return new CategoryDTO(updatedCategory);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
	}

	@Override
	public CategoryDTO getCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
	
		if(category.isPresent() == false) { return null; }
	
		return new CategoryDTO(category.get());
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}
	
	
	
	

}