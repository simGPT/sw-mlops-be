package com.springboot.swmlopsbe.global.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.springboot.swmlopsbe.domain.product.entity.Product;
import com.springboot.swmlopsbe.domain.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final ProductRepository productRepository;

  @Override
  public void run(ApplicationArguments args) {
    if (productRepository.count() > 0) {
      return;
    }

    List<Product> products =
        List.of(
            // 립 제품
            Product.builder()
                .name("롬앤 쥬시 래스팅 틴트 #피그말리온")
                .price(9500)
                .stock(150)
                .imageUrl("/images/롬앤 쥬시 래스팅 틴트.jpeg")
                .build(),
            Product.builder()
                .name("에뛰드 루키루키 립스틱 #RD302")
                .price(12000)
                .stock(120)
                .imageUrl("/images/에뛰드 루키루키 립스틱.jpeg")
                .build(),
            Product.builder()
                .name("맥 립스틱 #레트로")
                .price(38000)
                .stock(80)
                .imageUrl("/images/맥 립스틱 #레트로.jpeg")
                .build(),
            Product.builder()
                .name("라네즈 립 슬리핑 마스크")
                .price(17000)
                .stock(200)
                .imageUrl("/images/라네즈 립 슬리핑 마스크.jpeg")
                .build(),
            Product.builder()
                .name("클리오 버터 립 글로우")
                .price(11000)
                .stock(130)
                .imageUrl("/images/클리오 버터 립 글로우.jpeg")
                .build(),

            // 아이 메이크업
            Product.builder()
                .name("클리오 킬래쉬 마스카라")
                .price(16000)
                .stock(100)
                .imageUrl("/images/클리오 킬래쉬 마스카라.jpeg")
                .build(),
            Product.builder()
                .name("헤라 블랙 파운데이션 아이라이너")
                .price(19000)
                .stock(90)
                .imageUrl("/images/헤라 블랙 파운데이션 아이라이너.jpeg")
                .build(),
            Product.builder()
                .name("에뛰드 플레이 컬러 아이즈 #피치팜")
                .price(18000)
                .stock(70)
                .imageUrl("/images/에뛰드 플레이 컬러 아이즈.jpeg")
                .build(),
            Product.builder()
                .name("부르조아 볼륨 글라무르 마스카라")
                .price(14000)
                .stock(110)
                .imageUrl("/images/부르조아 볼륨 글라무르 마스카라.jpeg")
                .build(),
            Product.builder()
                .name("나스 아이섀도 팔레트 #언더그라운드")
                .price(68000)
                .stock(40)
                .imageUrl("/images/나스 아이섀도 팔레트.jpeg")
                .build(),

            // 베이스
            Product.builder()
                .name("헤라 블랙 쿠션 SPF34 #21C")
                .price(55000)
                .stock(60)
                .imageUrl("/images/헤라 블랙 쿠션.jpeg")
                .build(),
            Product.builder()
                .name("미샤 M 퍼펙트 커버 비비크림 #23")
                .price(9900)
                .stock(180)
                .imageUrl("/images/미샤 M 퍼펙트 커버 비비크림.jpeg")
                .build(),
            Product.builder()
                .name("나스 내추럴 래디언트 파운데이션 #시러큐스")
                .price(72000)
                .stock(50)
                .imageUrl("/images/나스 내추럴 래디언트 파운데이션.jpeg")
                .build(),
            Product.builder()
                .name("맥 스튜디오 픽스 파우더 #NC25")
                .price(45000)
                .stock(65)
                .imageUrl("/images/맥 스튜디오 픽스 파우더.jpeg")
                .build(),
            Product.builder()
                .name("클리오 킬 커버 파운웨어 쿠션 #03")
                .price(28000)
                .stock(95)
                .imageUrl("/images/클리오 킬 커버 파운웨어 쿠션.jpeg")
                .build(),

            // 스킨케어
            Product.builder()
                .name("이니스프리 그린티 씨드 세럼")
                .price(26000)
                .stock(200)
                .imageUrl("/images/이니스프리 그린티 씨드 세럼.jpeg")
                .build(),
            Product.builder()
                .name("라네즈 워터뱅크 블루 히알루론 크림")
                .price(42000)
                .stock(130)
                .imageUrl("/images/라네즈 워터뱅크 블루 히알루론 크림.jpeg")
                .build(),
            Product.builder()
                .name("아모레퍼시픽 트리트먼트 에센스")
                .price(130000)
                .stock(35)
                .imageUrl("/images/아모레퍼시픽 트리트먼트 에센스.jpeg")
                .build(),
            Product.builder()
                .name("코스알엑스 어드밴스드 스네일 96 무신 에센스")
                .price(18000)
                .stock(250)
                .imageUrl("/images/코스알엑스 어드밴스드 스네일 96 무신 에센스.jpeg")
                .build(),

            // 선케어
            Product.builder()
                .name("이니스프리 퍼펙트 UV 트리플 케어 선크림 SPF50+")
                .price(16000)
                .stock(170)
                .imageUrl("/images/이니스프리 퍼펙트 UV 트리플 케어 선크림.jpeg")
                .build(),
            Product.builder()
                .name("라운드랩 자작나무 수분 선크림 SPF50+")
                .price(12000)
                .stock(220)
                .imageUrl("/images/라운드랩 자작나무 수분 선크림.jpeg")
                .build(),
            Product.builder()
                .name("아비브 어린잎 선크림 SPF50+")
                .price(21000)
                .stock(160)
                .imageUrl("/images/아비브 어린잎 선크림.jpeg")
                .build());

    productRepository.saveAll(products);
    log.info("[DataInitializer] 상품 더미데이터 {}개 삽입 완료", products.size());
  }
}
