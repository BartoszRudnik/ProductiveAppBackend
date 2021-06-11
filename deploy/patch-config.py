#!/usr/bin/env python3
import yaml

db_creds_path = "db-creds.env"
mail_config_path = "mail-conf.yml"

app_config_path = "application.yml"

db_host_name = "db"

try:
    from yaml import CLoader as YamlLoader, CDumper as YamlDumper
except ImportError:
    from yaml import Loader as YamlLoader, Dumper as YamlDumper
    
def yaml2data(yaml_path):
	with open(yaml_path, "r", encoding = "utf-8") as file:
		return yaml.load(file, Loader = YamlLoader)
		
def data2yaml(yaml_path, data):
	with open(yaml_path, "w", encoding = "utf-8") as file:
		yaml.dump(data, Dumper = YamlDumper, stream = file)

db_creds = {}
with open(db_creds_path, "r", encoding = "utf-8") as file:
    for file_line in file:
        var_name, var_value = file_line.partition("=")[::2]
        db_creds[var_name.strip()] = var_value.strip()

app_config = yaml2data(app_config_path)

# Sets new db credentials
app_db_config = app_config["spring"]["datasource"]
app_db_config["url"] = "jdbc:postgresql://{}:5432/{}".format(
    db_host_name, db_creds["POSTGRES_DB"])
app_db_config["username"] = db_creds["POSTGRES_USER"]
app_db_config["password"] = db_creds["POSTGRES_PASSWORD"]

# Removes db debug settings
app_jpa_config = app_config["spring"]["jpa"]
app_jpa_config["hibernate"]["ddl-auto"] = "update"
app_jpa_config["show-sql"] = False

# Sets new mail config
app_config["spring"]["mail"] = yaml2data(mail_config_path)
 
# Saves new config
data2yaml(app_config_path, app_config)

print("Successfully updated spring app config!")
