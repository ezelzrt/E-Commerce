USE products;

INSERT INTO `users` (`id`, `access_type`, `address`, `age`, `email`, `first_name`, `gender`, `is_email_verified`, `last_name`, `password`, `photo`) VALUES ('10', '1', 'ubicacion re loca', '20', 'a@a', 'NombreAdmin', 'masc', b'0', 'Apellido', '$2a$10$CZCG4o08qkUJCfk9oqISe.wAFLxgrjz11uqW1xKzik49GM0u6wjeS', 'aaa'),
('11', '2', 'ubicacion re loca', '20', 'q@q', 'NombreCustomer', 'masc', b'0', 'Apellido', '$2a$10$CZCG4o08qkUJCfk9oqISe.wAFLxgrjz11uqW1xKzik49GM0u6wjeS', 'aaa');
USE products;

INSERT INTO `tb_master_product` (`id`, `base_price`, `description`, `name`) VALUES 
(1, 10000, 'A t-shirt', 'T-shirt'), 
(2, 16000, 'A pair of pants', 'Pants'), 
(3, 900, 'A fork', 'Fork'),
(4, 20000, 'A wool coat', 'Wool Coat'),
(5, 12000, 'A pair of sports sneakers', 'Sports Sneakers'),
(6, 5000, 'A wristwatch', 'Wristwatch'),
(7, 2500, 'A pair of leather gloves', 'Leather Gloves'),
(8, 700, 'A ceramic mug', 'Ceramic Mug'),
(9, 13000, 'A school backpack', 'School Backpack'),
(10, 800, 'A pair of sunglasses', 'Sunglasses'),
(11, 15000, 'A remote control toy car', 'Toy Car'),
(12, 18000, 'A mountain bike', 'Mountain Bike'),
(13, 3000, 'A cotton sheet set', 'Sheet Set'),
(14, 5500, 'A basic tool set', 'Tool Set'),
(15, 200, 'A pencil', 'Pencil'),
(16, 3000, 'A desk lamp', 'Desk Lamp'),
(17, 1200, 'A leather wallet', 'Leather Wallet'),
(18, 8000, 'An acoustic guitar', 'Acoustic Guitar'),
(19, 400, 'A pair of socks', 'Socks'),
(20, 2200, 'A reusable water bottle', 'Reusable Bottle'),
(21, 10000, 'A memory foam pillow', 'Memory Foam Pillow'),
(22, 11000, 'A pair of bluetooth headphones', 'Bluetooth Headphones'),
(23, 18000, 'A portable speaker', 'Portable Speaker'),
(24, 2500, 'A pair of shorts', 'Shorts');

INSERT INTO `tb_attributes` (`id`, `description`, `name`) VALUES 
(1, 'Color', 'Color'), 
(2, 'Measurement ', 'Clothing size'), 
(3, 'Has print', 'Print'),
(4, 'Material', 'Material'),
(5, 'Size', 'Size'),
(6, 'Class', 'Class'),
(7, 'Brand', 'Brand'),
(8, 'Gender', 'Gender'),
(9, 'Season', 'Season'),
(10, 'Weight', 'Weight'),
(11, 'Fragile', 'Fragile');

INSERT INTO `tb_attribute_values` (`id`, `value`, `attribute_id`) VALUES 
(1, 'Red', 1), 
(2, 'Green', 1), 
(3, 'Blue', 1), 
(4, '11 (Eleven)', 2), 
(5, '12 (Twelve)', 2), 
(6, 'Pikachu print', 3), 
(7, 'No print', 3), 
(8, 'Cotton', 4), 
(9, 'Leather', 4), 
(10, 'Polyester', 4), 
(11, 'Small', 5), 
(12, 'Medium', 5), 
(13, 'Large', 5), 
(14, 'Sporty', 6), 
(15, 'Casual', 6), 
(16, 'Elegant', 6), 
(17, 'Adidas', 7), 
(18, 'Nike', 7), 
(19, 'Puma', 7), 
(20, 'Male', 8), 
(21, 'Female', 8), 
(22, 'Unisex', 8), 
(23, 'Summer', 9), 
(24, 'Winter', 9), 
(25, 'Spring', 9),
(26, '0.1' ,10),
(27, '0.2' ,10),
(28, '0.5' ,10),
(29, '0.8' ,10),
(30, '1' ,10),
(31, '2' , 10),
(32, '3' ,10),
(33, '4' ,10),
(34, '5' ,10),
(35, 'No' ,11),
(36, 'Yes' ,11);

INSERT INTO `tb_master_product_attribute` (`masterproduct_id`, `attribute_id`) VALUES 
(1, 1),  -- T-shirt: Color
(1, 2),  -- T-shirt: Size
(1, 3),  -- T-shirt: Print
(2, 1),  -- Pants: Color
(2, 2),  -- Pants: Size
(3, 1),  -- Fork: Color
(4, 1),  -- Wool Coat: Color
(4, 4),  -- Wool Coat: Material
(5, 1),  -- Sports Sneakers: Color
(5, 6),  -- Sports Sneakers: Class
(6, 1),  -- Wristwatch: Color
(6, 7),  -- Wristwatch: Brand
(7, 4),  -- Leather Gloves: Material
(7, 8),  -- Leather Gloves: Gender
(8, 1),  -- Ceramic Mug: Color
(9, 5),  -- School Backpack: Size
(9, 7),  -- School Backpack: Brand
(10, 1),  -- Sunglasses: Color
(10, 6),  -- Sunglasses: Class
(11, 3),  -- Toy Car: Print
(12, 5),  -- Mountain Bike: Size
(13, 5),  -- Sheet Set: Size
(14, 4),  -- Tool Set: Material
(14, 6),  -- Tool Set: Class
(15, 1),  -- Pencil: Color
(16, 6),  -- Desk Lamp: Class
(17, 4),  -- Leather Wallet: Material
(18, 7),  -- Acoustic Guitar: Brand
(19, 1),  -- Socks: Color
(19, 2),  -- Socks: Size
(20, 5),  -- Reusable Bottle: Size
(21, 4),  -- Memory Foam Pillow: Material
(22, 7),  -- Bluetooth Headphones: Brand
(23, 6),  -- Portable Speaker: Class
(24, 1),  -- Shorts: Color
(8, 10), 
(8, 11);

INSERT INTO `tb_master_product_attribute_relation` (`attribute_id`, `value_id`, `masterproduct_id`) VALUES 
(1, 1, 1),  -- T-shirt: Color = Red
(1, 3, 1),  -- T-shirt: Color = Blue
(2, 4, 1),  -- T-shirt: Size = 11 (Eleven)
(2, 5, 1),  -- T-shirt: Size = 12 (Twelve)
(3, 6, 1),  -- T-shirt: Print = Pikachu print
(3, 7, 1),  -- T-shirt: Print = No print
(1, 1, 2),  -- Pants: Color = Red
(1, 2, 2),  -- Pants: Color = Green
(2, 4, 2),  -- Pants: Size = 11 (Eleven)
(2, 5, 2),  -- Pants: Size = 12 (Twelve)
(1, 1, 3),  -- Fork: Color = Red
(4, 8, 4),  -- Wool Coat: Material = Wool
(4, 9, 4),  -- Wool Coat: Material = Leather
(1, 3, 5),  -- Sports Sneakers: Color = Blue
(1, 2, 5),  -- Sports Sneakers: Color = Green
(6, 10, 5),  -- Sports Sneakers: Class = Sporty
(7, 11, 6),  -- Wristwatch: Brand = Casio
(1, 3, 6),  -- Wristwatch: Color = Blue
(8, 12, 7),  -- Leather Gloves: Gender = Male
(8, 13, 7),  -- Leather Gloves: Gender = Female
(4, 9, 7),  -- Leather Gloves: Material = Leather
(1, 2, 8),  -- Ceramic Mug: Color = White
(1, 3, 8),  -- Ceramic Mug: Color = Blue
(5, 14, 9),  -- School Backpack: Size = Small
(5, 15, 9),  -- School Backpack: Size = Medium
(7, 16, 9),  -- School Backpack: Brand = Adidas
(1, 1, 10),  -- Sunglasses: Color = Red
(6, 17, 10),  -- Sunglasses: Class = Sun
(1, 1, 11),  -- Toy Car: Color = Red
(3, 6, 11),  -- Toy Car: Print = Pikachu print
(5, 18, 12),  -- Mountain Bike: Size = Large
(4, 9, 13),  -- Sheet Set: Material = Cotton
(6, 19, 14),  -- Tool Set: Class = Professional
(6, 20, 14),  -- Tool Set: Class = Home
(1, 1, 15),  -- Pencil: Color = Yellow
(2, 4, 16),  -- Desk Lamp: Size = Medium
(4, 9, 17),  -- Leather Wallet: Material = Leather
(7, 21, 18),  -- Acoustic Guitar: Brand = Fender
(1, 2, 19),  -- Socks: Color = White
(2, 5, 20),  -- Reusable Bottle: Size = Medium
(4, 22, 21),  -- Memory Foam Pillow: Material = Memory Foam
(7, 23, 22),  -- Bluetooth Headphones: Brand = Sony
(6, 24, 23),  -- Portable Speaker: Class = Wireless
(1, 1, 24),  -- Shorts: Color = Green
(10, 28, 8),
(10, 29, 8),
(10, 30, 8),
(10, 34, 8),
(11, 35, 8),
(11, 36, 8);



INSERT INTO `tb_product` (`id`, `compromised_stock`, `image_url`, `name`, `price`, `stock`, `master_product_id`) VALUES
(1, 0, 'box.svg', 'T-shirt Red 11 Pikachu', 10000, 5, 1),
(2, 0, 'box.svg', 'T-shirt Red 11 ', 10000, 4, 1),
(3, 0, 'box.svg', 'T-shirt Red 12 Pikachu', 10000, 421, 1),
(4, 0, 'box.svg', 'T-shirt Red 12', 10000, 124, 1),
(5, 0, 'box.svg', 'T-shirt Blue 11 Pikachu', 10000, 412, 1),
(6, 0, 'box.svg', 'T-shirt Blue 12 Pikachu', 10000, 0, 1),
(7, 0, 'box.svg', 'T-shirt Blue 12', 10000, 421, 1),
(8, 0, 'box.svg', 'Ceramic Mug Big Fragile', 700, 110, 8),
(9, 0, 'box.svg', 'Ceramic Mug Medium Fragile', 700, 123, 8),
(10, 0, 'box.svg', 'Ceramic Mug Small Fragile', 700, 412, 8),
(11, 0, 'box.svg', 'Ceramic Mug Small', 700, 32, 8),
(12, 0, 'box.svg', 'Ceramic Mug Medium', 700, 41, 8),
(13, 0, 'box.svg', 'Red fork', 900, 441, 3),
(14, 0, 'box.svg', 'Blue fork', 900, 1231, 3),
(15, 0, 'box.svg', 'Green pencil', 200, 421, 15),
(16, 0, 'box.svg', 'Blue pencil', 200, 12, 15),
(17, 0, 'box.svg', 'Pants 11 Red', 16000, 155, 2),
(18, 0, 'box.svg', 'Pants 12 Red', 16000, 652, 2),
(19, 0, 'box.svg', 'Pants 11 Green', 16000, 73, 2);

INSERT INTO `tb_product_attribute_value` (`product_id`, `attribute_value_id`) VALUES
(2, 4),
(1, 1),
(1, 4),
(1, 6),
(2, 7),
(2, 1),
(3, 5),
(3, 6),
(3, 1),
(4, 5),
(4, 7),
(4, 1),
(5, 4),
(5, 6),
(5, 3),
(6, 5),
(6, 6),
(6, 3),
(7, 5),
(7, 7),
(7, 3),
(8, 1),
(8, 34),
(8, 36),
(9, 1),
(9, 30),
(9, 36),
(10, 1),
(10, 28),
(10, 36),
(11, 1),
(11, 28),
(11, 35),
(12, 2),
(12, 30),
(12, 35),
(13, 1),
(14, 3),
(15, 2),
(16, 3),
(17, 1),
(17, 4),
(18, 1),
(18, 5),
(19, 2),
(19, 4);

INSERT INTO tb_rules (id, operation, rule_group_id, rules_follower_id, rules_follower_type, value) 
SELECT id, 'LOWER', id, id, 'PRODUCT', 2 FROM tb_product;
