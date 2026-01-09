create table if not exists desks (
    id bigserial primary key,
    name varchar(80) not null unique,
    location varchar(120) not null,
    active boolean not null default true
);

create table if not exists employees (
    id bigserial primary key,
    username varchar(120) not null unique,
    display_name varchar(255) not null
);

create table if not exists reservations (
    id uuid primary key,
    desk_id bigint not null references desks(id),
    employee_id bigint not null references employees(id),
    start_date date not null,
    end_date date not null,
    status varchar(20) not null
);

create index if not exists idx_reservations_employee on reservations(employee_id);
create index if not exists idx_reservations_desk on reservations(desk_id);
