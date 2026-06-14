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
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/ec45824c-d9b0-4e7e-ac3b-5b7f14a2271f_롬앤 쥬시 래스팅 틴트.jpeg")
                .build(),
            Product.builder()
                .name("에뛰드 루키루키 립스틱 #RD302")
                .price(12000)
                .stock(120)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/c4ffae14-2a8c-42b0-a9fe-1fa86e085cea_에뛰드 루키루키 립스틱.jpeg")
                .build(),
            Product.builder()
                .name("맥 립스틱 #레트로")
                .price(38000)
                .stock(80)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/3b81e470-b33a-4eec-9235-890347abfd7e_맥 립스틱 #레트로.jpeg")
                .build(),
            Product.builder()
                .name("라네즈 립 슬리핑 마스크")
                .price(17000)
                .stock(200)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/94b65c55-1902-48a5-a482-b179fd88e143_라네즈 립 슬리핑 마스크.jpeg")
                .build(),
            Product.builder()
                .name("클리오 버터 립 글로우")
                .price(11000)
                .stock(130)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/2d602522-0128-44a7-87a3-f420ace5cb3f_클리오 버터 립 글로우.jpeg")
                .build(),

            // 아이 메이크업
            Product.builder()
                .name("클리오 킬래쉬 마스카라")
                .price(16000)
                .stock(100)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/c55a7960-19f3-4611-b5fc-2848ff6163c9_클리오 킬래쉬 마스카라.jpeg")
                .build(),
            Product.builder()
                .name("헤라 블랙 파운데이션 아이라이너")
                .price(19000)
                .stock(90)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/fd3ab275-ffc7-491d-aa3f-fc8b1378fa94_헤라 블랙 파운데이션 아이라이너.jpeg")
                .build(),
            Product.builder()
                .name("에뛰드 플레이 컬러 아이즈 #피치팜")
                .price(18000)
                .stock(70)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/b08cf48f-740f-4697-9836-1d446146bb74_에뛰드 플레이 컬러 아이즈.jpeg")
                .build(),
            Product.builder()
                .name("부르조아 볼륨 글라무르 마스카라")
                .price(14000)
                .stock(110)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/3d4e5845-35f3-4115-b022-929dfd2ec74a_부르조아 볼륨 글라무르 마스카라.jpeg")
                .build(),
            Product.builder()
                .name("나스 아이섀도 팔레트 #언더그라운드")
                .price(68000)
                .stock(40)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/c7d453c3-2e40-447f-9f52-690abd71e4e9_나스 아이섀도 팔레트.jpeg")
                .build(),

            // 베이스
            Product.builder()
                .name("헤라 블랙 쿠션 SPF34 #21C")
                .price(55000)
                .stock(60)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/a449419a-67f3-43a6-9af7-b9371143e889_헤라 블랙 쿠션.jpeg")
                .build(),
            Product.builder()
                .name("미샤 M 퍼펙트 커버 비비크림 #23")
                .price(9900)
                .stock(180)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/d397cf09-4c94-41b8-b4bc-017326d2817a_미샤 M 퍼펙트 커버 비비크림.jpeg")
                .build(),
            Product.builder()
                .name("나스 내추럴 래디언트 파운데이션 #시러큐스")
                .price(72000)
                .stock(50)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/2e557611-11b7-4b11-acf4-2ccf30ef9bf4_나스 내추럴 래디언트 파운데이션.jpeg")
                .build(),
            Product.builder()
                .name("맥 스튜디오 픽스 파우더 #NC25")
                .price(45000)
                .stock(65)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/6ab302bc-8fc4-48ae-9f52-6730326689cd_맥 스튜디오 픽스 파우더.jpeg")
                .build(),
            Product.builder()
                .name("클리오 킬 커버 파운웨어 쿠션 #03")
                .price(28000)
                .stock(95)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/cafa0393-bbd3-476e-9b28-3356f681d524_클리오 킬 커버 파운웨어 쿠션.jpeg")
                .build(),

            // 스킨케어
            Product.builder()
                .name("이니스프리 그린티 씨드 세럼")
                .price(26000)
                .stock(200)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/ab99e9f2-8363-4ae7-b03b-81702aca0afe_이니스프리 그린티 씨드 세럼.jpeg")
                .build(),
            Product.builder()
                .name("라네즈 워터뱅크 블루 히알루론 크림")
                .price(42000)
                .stock(130)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/d848d6dc-c212-4cdc-acb5-4bac5bf9cd2c_라네즈 워터뱅크 블루 히알루론 크림.jpeg")
                .build(),
            Product.builder()
                .name("아모레퍼시픽 트리트먼트 에센스")
                .price(130000)
                .stock(35)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/413a77e4-74c8-49bd-b3db-3df12fa208c4_아모레퍼시픽 트리트먼트 에센스.jpeg")
                .build(),
            Product.builder()
                .name("코스알엑스 어드밴스드 스네일 96 무신 에센스")
                .price(18000)
                .stock(250)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/889e290a-db3a-4c76-85dc-1dd4eb0abf67_코스알엑스 어드밴스드 스네일 96 무신 에센스.jpeg")
                .build(),

            // 선케어
            Product.builder()
                .name("이니스프리 퍼펙트 UV 트리플 케어 선크림 SPF50+")
                .price(16000)
                .stock(170)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/ae233404-517d-4873-a279-59307bc4e4f3_이니스프리 퍼펙트 UV 트리플 케어 선크림.jpeg")
                .build(),
            Product.builder()
                .name("라운드랩 자작나무 수분 선크림 SPF50+")
                .price(12000)
                .stock(220)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/26381ba0-0bb1-48ee-857c-ed82ad02ed29_라운드랩 자작나무 수분 선크림.jpeg")
                .build(),
            Product.builder()
                .name("아비브 어린잎 선크림 SPF50+")
                .price(21000)
                .stock(160)
                .imageUrl(
                    "https://sw-mlops-be-bucket.s3.ap-northeast-2.amazonaws.com/products/e9c7ce7b-07f8-49c0-ac6b-fe6a90db4090_아비브 어린잎 선크림.jpeg")
                .build());

    productRepository.saveAll(products);
    log.info("[DataInitializer] 상품 더미데이터 {}개 삽입 완료", products.size());
  }
}
