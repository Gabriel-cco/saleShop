package br.com.sale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sale.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
