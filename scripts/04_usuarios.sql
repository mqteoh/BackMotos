SET password_encryption = 'scram-sha-256';

CREATE ROLE app_motos LOGIN PASSWORD 'CAMBIAR_ESTA_CLAVE';
GRANT CONNECT ON DATABASE motomaster TO app_motos;
GRANT USAGE ON SCHEMA concesionario_motos TO app_motos;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA concesionario_motos TO app_motos;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA concesionario_motos TO app_motos;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA concesionario_motos TO app_motos;

CREATE ROLE consulta_motos LOGIN PASSWORD 'CAMBIAR_ESTA_CLAVE';
GRANT CONNECT ON DATABASE motomaster TO consulta_motos;
GRANT USAGE ON SCHEMA concesionario_motos TO consulta_motos;
GRANT SELECT ON ALL TABLES IN SCHEMA concesionario_motos TO consulta_motos;
