#!/usr/bin/env python3
import yaml

db_creds_path = "db-creds.env"
app_config_path = "application.yml"

db_host_name = "db"

try:
    from yaml import CLoader as YamlLoader, CDumper as YamlDumper
except ImportError:
    from yaml import Loader as YamlLoader, Dumper as YamlDumper

db_credentials = {}
with open(db_creds_path, "r", encoding = "utf-8") as file:
    for file_line in file:
        var_name, var_value = file_line.partition("=")[::2]
        db_credentials[var_name.strip()] = var_value.strip()

app_config = {}
with open(app_config_path, "r", encoding = "utf-8") as file:
    app_config = yaml.load(file, Loader = YamlLoader)

app_db_config = app_config["spring"]["datasource"]
app_db_config["url"] = "jdbc:postgresql://{}:5432/{}".format(
    db_host_name, db_credentials["POSTGRES_DB"])
app_db_config["username"] = db_credentials["POSTGRES_USER"]
app_db_config["password"] = db_credentials["POSTGRES_PASSWORD"]

with open(app_config_path, "w", encoding = "utf-8") as file:
    yaml.dump(app_config, Dumper = YamlDumper, stream = file)

print("Successfully updated spring app config!")
