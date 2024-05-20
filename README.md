# 주민정보관리 데이터베이스
### 요구사항
주민관리 예시 문서를 보고, 데이터 모델링을 수행 후 DBMS에 테이블을 생성합니다.
그리고 예시의 가족관계증명서와 주민등록등본에 대한 데이터를 입력 후 조회하는 SQL을 작성하는 문제입니다.
[(평가실습문제) NHN Academy_데이터베이스 설계&SQL.pdf](https://github.com/lettuce82/nhnacademy/files/14985111/NHN.Academy_.SQL.pdf)
### 이슈
##### issue 1
가족과 세대주-세대원 등의 개념을 엔티티 간의 관계로 볼 것인지 하나의 개체로 볼 것인지에 대하여 혼란이 있었다.
과제 제출시에 가족은 ImmediateFamily 테이블의 FamilyNo 이 동일한 엔티티가 동일한 가족으로 구분하여 가족을 하나의 개체로 판단하였고,
세대주와 세대원 역시 Household 테이블의 HouseholdId 가 동일한 엔티티는 동일한 세대에 속하는 개체로 판단하여 설계하였다.
##### issue 2
전입주소 변경과 세대 변경의 차이는 고려하지 못 해 AddressHistory 테이블에 주소 정보와 변동사유 정보를 함께 저장하였다.
##### issue 3
Person 테이블에는 개인의 기본정보가 들어 있는데 부가정보인 생사 여부를 판단하는 aliveNo과 주소 식별 정보인 AddressId 도 포함하여 설계하였다.

![스크린샷 2024-04-16 오전 9 03 28](https://github.com/lettuce82/nhnacademy/assets/152861713/df800c0d-2b54-4068-81e0-c040f6dcb00c)
