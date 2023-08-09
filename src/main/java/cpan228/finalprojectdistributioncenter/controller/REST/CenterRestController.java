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
@RequestMapping(path = "/api/center", produces = "application/json")
public class CenterRestController {

    private final DistributionCenterRepositoryPaginated centerRepositoryPaginated;
    private final DistributionCenterRepository centerRepository;

    private final ItemRepository itemRepository;

    public CenterRestController(DistributionCenterRepositoryPaginated centerRepositoryPaginated,
            DistributionCenterRepository centerRepository, ItemRepository itemRepository) {
        this.centerRepositoryPaginated = centerRepositoryPaginated;
        this.centerRepository = centerRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public Iterable<DistributionCenter> allCenters(@RequestParam("page") Optional<Integer> page,
                                                   @RequestParam("size") Optional<Integer> size) {
        if (!page.isPresent() || !size.isPresent()) {
            return centerRepository.findAll();
        } else {
            return centerRepositoryPaginated.findAll(PageRequest.of(page.get(), size.get()));
        }
    }

    @GetMapping ("/{id}/items")
    public List<Item> getCenterItems(@PathVariable Long id) {
        var selectedCenter = centerRepository.findById(id);
        var items = selectedCenter.get().getItems();
        return items;
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

    @PostMapping
    public DistributionCenter createCenter(@Valid @RequestBody CreateCenter center) {
        return centerRepository.save(center.toDistributionCenter());
    }

    @PostMapping("/{id}/items")
    public Item addItemToCenter(@PathVariable Long id, @Valid @RequestBody Item item) {
        var selectedCenter = centerRepository.findById(id);
        item.setDistributionCenter(selectedCenter.get());
        var savedItem = itemRepository.save(item);
        return savedItem;
    }

    @PutMapping("/{id}")
    public DistributionCenter updateCenter(@PathVariable("id") Long id,
            @Valid @RequestBody CreateCenter center) {
        var centerToUpdate = centerRepository.findById(id).orElseThrow();
        centerToUpdate.setName(center.getName());
        centerToUpdate.setItems(center.getItems());
        centerToUpdate.setLatitude(center.getLatitude());
        centerToUpdate.setLongitude(center.getLongitude());
        return centerRepository.save(center.toDistributionCenter());
    }

}
