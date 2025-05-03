INSERT INTO users (id, email, firstname, lastname, password, role)
VALUES (UUID_TO_BIN('53c3139d-9213-4e6d-bb4d-aa2168d38a9e'), 'admin@gmail.com', 'admin', 'admin',
        '$2a$12$lVJtPYap0O4AM.jNpV8eYeDzTYuVZlDmGLTWZ0BxPbcYfbfusfzq2', 'ADMIN'),
        (UUID_TO_BIN('db359295-b365-44ef-83eb-41ebcac62d20'), 'carreramatias12@gmail.com', 'Matias', 'Carrera',
                '$2a$12$3QmyP5Ad4l/o4082KVPpmuaqtzjEaDlSVE2zMeBILbykC17OJKBbi', 'USER');

INSERT INTO categories (id, description, image, name)
VALUES (UUID_TO_BIN('450afedc-e204-4559-be3f-28de5bedfdab'), 'Viví una experiencia mágica rodeado de naturaleza, desde las alturas.',
        'casa-arbol.svg', 'Casas del arbol'),
       (UUID_TO_BIN('a372c81a-73c0-4d7c-be83-35f1415e8514'), 'Despertate con el sonido del mar y disfrutá del sol y la arena a pasos de tu alojamiento.',
        'playa.svg', 'Frente a la playa'),
       (UUID_TO_BIN('559d72fb-0785-475a-8b99-1c85283bd6bc'), 'Relajate en un entorno rural, lejos del ruido de la ciudad.', 'casa-campo.svg',
        'Casas de campo'),
       (UUID_TO_BIN('1d3b1594-051f-4f34-b3f6-83c6151681ac'), 'Alojate en lugares elevados con vistas increíbles, como montañas o terrazas panorámicas.',
        'en-las-alturas.svg', 'En las alturas'),
       (UUID_TO_BIN('bc221331-f9ae-4d1e-ac90-7d829c1a2d5a'), 'Espacios compactos pero funcionales, pensados para una estadía minimalista.',
       'modern-house.svg', 'Diseño'),
       (UUID_TO_BIN('41d55c7e-1f2c-479a-bfd7-e4ba5f7e969a'), 'Alojamientos sobre ruedas para los amantes del movimiento y la libertad.',
       'casas-rodantes.svg', 'Casas rodantes'),
       (UUID_TO_BIN('24d133b1-d3c3-4706-ac9a-1309ac66bee1'), 'Conectá con la naturaleza en carpas, domos o refugios al aire libre', 'camping.svg',
        'Camping'),
        (UUID_TO_BIN('a2107475-9bdd-4d10-89c8-3a7f0511a96a'), 'Espacios privados dentro de un hogar compartido, ideales para viajeros prácticos.',
        'habitaciones.svg', 'Habitaciones'),
        (UUID_TO_BIN('5e3c1f4b-c583-4d57-9cd7-5b8236af5de4'), 'Alojamiento con desayuno incluido, ideal para estadías cómodas y hogareñas.',
        'bed&breakfast.svg', 'Bed & Breakfast'),
        (UUID_TO_BIN('833771e4-20bd-4b1c-b9ca-cdde84023dc4'), 'Alojamientos rurales y tranquilos en el campo',
        'cabana.svg', 'Cabaña');

INSERT INTO amenities (id, icon, name)
VALUES (UUID_TO_BIN('1c9f77b1-1be1-4b18-a43b-287b0989238e'), 'wifi.svg', 'Wi-fi'),
       (UUID_TO_BIN('2dca07fc-7c1d-4069-9057-f5c55a1c0ab1'), 'air-conditioner.svg', 'Aire Acondicionado'),
       (UUID_TO_BIN('9f8c1be1-6200-4533-a1fa-c23c12d0e0b4'), 'shower.svg', 'Ducha'),
       (UUID_TO_BIN('f8c2ab10-f70a-467e-92a1-f3ffeb1266e0'), 'kitchen.svg', 'Cocina'),
       (UUID_TO_BIN('61f63e53-5092-450b-b17c-0e340d1258d0'), 'bathroom.svg', 'Baño'),
       (UUID_TO_BIN('5676c57b-d988-4044-b9ff-4674de90d1c7'), 'pool.svg', 'Ppiletaiscina'),
       (UUID_TO_BIN('db98265e-d3b4-47cc-a35d-97b9ab3143ec'), 'gym.svg', 'Gimnasio'),
       (UUID_TO_BIN('b091d98c-465b-489a-9835-1e58be88ef52'), 'tv.svg', 'Televisión'),
       (UUID_TO_BIN('1d18ac34-7b37-4e7d-a71f-10a7f6acb364'), 'parking.svg', 'Estacionamiento'),
       (UUID_TO_BIN('a278d2c3-e45d-4f76-a6f6-440b7e7de1b0'), 'jacuzzi.svg', 'Jacuzzi'),
       (UUID_TO_BIN('3b674d7c-7e78-437d-970d-c548d3b1b007'), 'terrace.svg', 'Terraza'),
       (UUID_TO_BIN('fe9f0734-33c2-44d1-94a2-3fe1b76c35e9'), 'balcony.svg', 'Balcón'),
       (UUID_TO_BIN('dda5c078-36c6-4601-8b49-13b289cf0e6f'), 'oven.svg.svg', 'Cocina equipada'),
       (UUID_TO_BIN('8105d467-b09d-4631-b5c1-d2f8b5d8f110'), 'chimney.svg', 'Chimenea'),
       (UUID_TO_BIN('4f21d2f0-63c3-41e9-b91d-8c81f83a9688'), 'laundry.svg', 'Lavadora');