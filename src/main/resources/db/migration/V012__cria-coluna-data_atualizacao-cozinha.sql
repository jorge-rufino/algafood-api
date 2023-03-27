alter table cozinha add data_atualizacao datetime null;
update cozinha set data_atualizacao = utc_timestamp();
alter table cozinha modify data_atualizacao datetime not null;