package jhyun.cbr.storage.repositories;

import jhyun.cbr.storage.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findOneByUsername(String username);
}
