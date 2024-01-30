package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    // ModelAttribute : 해당 컨트롤러를 요청할 때 'regions'에서 반환한 값이 자동으로 모델('model')에 담기게 된다.
    public Map<String, String> regions() {
        // HashMap은 순서 보장이 안되기 때문에 LinkedHashMap을 사용한다.
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    };

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        // ENUM의 모든 정보를 배열로 반환한다. (BOOK, FOOD, ETC)
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // form에서 thymeleaf를 사용하려면 빈 아이템이라도 넘겨줘야한다.
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        /*
            판매여부 체크박스 확인
            item.open=true - 체크 박스를 선택하는 경우
            item.open=null - 체크 박스를 선택하지 않는 경우

            HTML Form에서는 'open=on' 이라는 값이 넘어가는데,
            스프링은 'on'을 'true'타입으로 변환해준다. (스프링 타입 컨버터가 이 기능을 수행한다.)

            HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 'open'이라는 필드 자체가 서버로 전송되지 않는다.
            수정의 경우에는 상황에 따라서 이 방식이 문제가 될 수 있다.
         */
        //log.info("item.open={}", item.getOpen());

        /* 등록지역 체크박스 확인 */
        log.info("item.regions={}", item.getRegions());

        /* 상품종류 체크박스 확인 */
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

