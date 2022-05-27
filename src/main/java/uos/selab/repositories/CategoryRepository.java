package uos.selab.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uos.selab.domains.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
}
