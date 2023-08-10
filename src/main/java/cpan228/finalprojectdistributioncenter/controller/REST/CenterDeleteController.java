package cpan228.finalprojectdistributioncenter.controller.REST;

import java.util.List;
import java.util.Optional;

import cpan228.finalprojectdistributioncenter.model.DistributionCenter;
import cpan228.finalprojectdistributioncenter.model.Item;
import cpan228.finalprojectdistributioncenter.model.dto.CreateCenter;
import cpan228.finalprojectdistributioncenter.repository.DistributionCenterRepository;
import cpan228.finalprojectdistributioncenter.repository.DistributionCenterRepositoryPaginated;
import cpan228.finalprojectdistributioncenter.repository.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "/api/delete", produces = "application/json")
public class CenterDeleteController {

    private final DistributionCenterRepository centerRepository;

    private final ItemRepository itemRepository;

    public CenterDeleteController(DistributionCenterRepository centerRepository, ItemRepository itemRepository) {
        this.centerRepository = centerRepository;
        this.itemRepository = itemRepository;
    }

    @DeleteMapping("/{id}")
    public void deleteCenter(@PathVariable("id") Long id) {
        centerRepository.deleteById(id);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public void deleteItemFromCenter(@PathVariable Long id, @PathVariable("itemId") Long itemId) {
        var selectedCenter = centerRepository.findById(id);
        var selectedCenterItems = selectedCenter.get().getItems();
        var itemToDelete = itemRepository.findById(itemId);
        selectedCenterItems.remove(itemToDelete.get());
        itemRepository.deleteById(itemId);
    }

    @DeleteMapping("/{id}/items/all")
    public void deleteAllItemsFromCenter(@PathVariable Long id) {
        var selectedCenter = centerRepository.findById(id);
        var selectedCenterItems = selectedCenter.get().getItems();
        selectedCenterItems.clear();
        itemRepository.deleteAll();
    }


}

