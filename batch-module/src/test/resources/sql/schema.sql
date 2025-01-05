create table products
(
    product_id      varchar(255) not null,               -- 상품 ID
    seller_id       bigint       not null,               -- 판매자 ID
    category        varchar(255) not null,               -- 카테고리
    product_name    varchar(255) not null,               -- 상품 이름
    sale_start_date date,                                -- 판매 시작일
    sale_end_date   date,                                -- 판매 종료일
    product_status  varchar(50),                         -- 상품 상태
    brand           varchar(255),                        -- 브랜드 이름
    manufacturer    varchar(255),                        -- 제조사 이름
    sales_price     int          not null,               -- 판매 가격
    stock_quantity  int       default 0,                 -- 재고 수량
    create_at       timestamp default current_timestamp, -- 생성일시
    update_at       timestamp default current_timestamp, -- 수정일시
    primary key (product_id)                             -- 기본 키 설정
);
create index idx_products_product_status on products (product_status);
create index idx_products_category on products (category);
create index idx_products_brand on products (brand);
create index idx_products_manufacturer on products (manufacturer);
create index idx_products_seller_id on products (seller_id);
