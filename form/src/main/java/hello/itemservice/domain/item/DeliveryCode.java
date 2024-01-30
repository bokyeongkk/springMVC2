package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */

@Data
@AllArgsConstructor
public class DeliveryCode {
    // 시스템에서 전달하는 값 (FAST)
    private String code;
    // 고객에게 보여주는 값 (빠른 배송)
    private String displayName;
}
