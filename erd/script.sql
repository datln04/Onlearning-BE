create table account
(
    active      boolean,
    role        smallint
        constraint account_role_check
            check ((role >= 0) AND (role <= 2)),
    created_at  timestamp(6) with time zone,
    deleted_at  timestamp(6) with time zone,
    id          bigserial
        primary key,
    updated_at  timestamp(6) with time zone,
    description varchar(1000),
    address     varchar(255),
    avatar      varchar(255),
    code        varchar(255),
    email       varchar(255),
    name        varchar(255),
    password    varchar(255),
    phone       varchar(255),
    username    varchar(255)
        unique
);

alter table account
    owner to postgres;

create table policy_config
(
    id   bigserial
        primary key,
    name varchar(255)
);

alter table policy_config
    owner to postgres;

create table profile
(
    account_id bigint
        unique
        constraint fklc4oipegt3vyph78q31itt3pf
            references account,
    id         bigserial
        primary key,
    address    varchar(255),
    email      varchar(255),
    full_name  varchar(255),
    phone      varchar(255)
);

alter table profile
    owner to postgres;

create table question_bank
(
    id      bigserial
        primary key,
    content varchar(255)
);

alter table question_bank
    owner to postgres;

create table student
(
    status        boolean,
    date_of_birth timestamp(6),
    id            bigserial
        primary key,
    address       varchar(255),
    email         varchar(255),
    fullname      varchar(255),
    password      varchar(255),
    phone         varchar(255),
    token         varchar(255),
    username      varchar(255)
);

alter table student
    owner to postgres;

create table notification
(
    read       boolean,
    status     boolean,
    trash      boolean,
    account_id bigint
        constraint fkj0b1ncedmpl7sx7t7o54t26v2
            references account,
    id         bigserial
        primary key,
    student_id bigint
        constraint fkopnvuweg49llrxcdnh7k47kt9
            references student,
    message    varchar(255),
    title      varchar(255)
);

alter table notification
    owner to postgres;

create table report
(
    status      boolean,
    account_id  bigint
        constraint fk437jjev4fj82pu6m07461qj0u
            references account,
    create_time timestamp(6),
    id          bigserial
        primary key,
    student_id  bigint
        constraint fkl6ssom7r83r8bofvdkjkp18pj
            references student,
    content     varchar(255)
);

alter table report
    owner to postgres;

create table student_wallet
(
    amount      double precision,
    id          bigserial
        primary key,
    student_id  bigint
        constraint fkkftomp8d7gy1ay1hhs13wcr99
            references student,
    bank_number varchar(255)
);

alter table student_wallet
    owner to postgres;

create table subject
(
    min_price   double precision not null,
    status      boolean,
    account_id  bigint
        constraint fk2ejr1lespr8ap73j0ssq86sh9
            references account,
    create_date timestamp(6),
    id          bigserial
        primary key,
    description varchar(255),
    name        varchar(255)
);

alter table subject
    owner to postgres;

create table course
(
    price       double precision,
    account_id  bigint
        constraint fkfpl8i4mi0ad6whxv3n7h4jrs6
            references account,
    create_date timestamp(6),
    end_date    timestamp(6),
    id          bigserial
        primary key,
    start_date  timestamp(6),
    subject_id  bigint
        constraint fkm1expnaas0onmafqpktmjixnx
            references subject,
    description varchar(255),
    image       varchar(255),
    name        varchar(255),
    status      varchar(255)
);

alter table course
    owner to postgres;

create table element_question_bank
(
    is_correct       boolean,
    id               bigserial
        primary key,
    question_bank_id bigint
        constraint fkkkrxigusalun2kqmrwl3w6f34
            references question_bank,
    content          varchar(255),
    account_id       bigint
        constraint fk6pmisxkml3l5h6kmj6th7wne4
            references account,
    course_id        bigint
        constraint fk7yhupopb3t546igrw5s3ct2d5
            references course
);

alter table element_question_bank
    owner to postgres;

create table enroll
(
    amount         double precision,
    course_id      bigint
        constraint fkiv2pkft2ab8mqx9ffc4ex4q7e
            references course,
    finish_date    timestamp(6),
    id             bigserial
        primary key,
    request_date   timestamp(6),
    student_id     bigint
        constraint fkr5hq653kinipvyshfdl738ouc
            references student,
    payment_status varchar(255),
    status         varchar(255)
);

alter table enroll
    owner to postgres;

create table feedback
(
    status     boolean,
    course_id  bigint
        constraint fkko7f08v61t5y67teh5jxxwrea
            references course,
    id         bigserial
        primary key,
    student_id bigint
        constraint fknx2ciug1tx4kx3nea6xwhghs5
            references student,
    content    varchar(255)
);

alter table feedback
    owner to postgres;

create table lesson
(
    time        integer not null,
    course_id   bigint
        constraint fkjs3c7skmg8bvdddok5lc7s807
            references course,
    date_time   timestamp(6),
    id          bigserial
        primary key,
    asset       varchar(255),
    description varchar(255),
    name        varchar(255),
    status      varchar(255),
    url         varchar(255)
);

alter table lesson
    owner to postgres;

create table quiz
(
    max_time   double precision,
    pass_score double precision,
    id         bigserial
        primary key,
    lesson_id  bigint
        constraint fki10d7n4lf738sm3mon21aubik
            references lesson,
    status     varchar(255),
    title      varchar(255),
    type       varchar(255)
);

alter table quiz
    owner to postgres;

create table question
(
    id      bigserial
        primary key,
    quiz_id bigint
        constraint fkb0yh0c1qaxfwlcnwo9dms2txf
            references quiz,
    content varchar(255)
);

alter table question
    owner to postgres;

create table element
(
    correct     boolean,
    id          bigserial
        primary key,
    question_id bigint
        constraint fkmfnh4qtkmabo9v2r86r5mec5c
            references question,
    content     varchar(255)
);

alter table element
    owner to postgres;

create table resource
(
    id            bigserial
        primary key,
    lesson_id     bigint
        constraint fkgpi9gjtubx2nwit2971txbqrm
            references lesson,
    content       varchar(255),
    resource_type varchar(255),
    study_time    varchar(255)
);

alter table resource
    owner to postgres;

create table result_quiz
(
    fail_count    integer not null,
    id            bigserial
        primary key,
    quiz_id       bigint
        constraint fkrsn0ubkknejds4mjb85ywmbbe
            references quiz,
    start_time    timestamp(6),
    student_id    bigint
        constraint fkb35rig40rp05so2d13kks0ck2
            references student,
    submit_time   timestamp(6),
    result_status varchar(255)
);

alter table result_quiz
    owner to postgres;

create table result_detail
(
    element_id     bigint
        constraint fk72ut351igf6qloyj19lcgeul6
            references element,
    id             bigserial
        primary key,
    selected_time  timestamp(6),
    result_quiz_id bigint
        constraint fkqjnaaop9v8os5v10lildb49ne
            references result_quiz
);

alter table result_detail
    owner to postgres;

create table transaction
(
    amount      double precision,
    id          bigserial
        primary key,
    student_id  bigint
        constraint fkd71rhpdlg8cjw7byfd16lg304
            references student,
    description varchar(255),
    status      varchar(255)
);

alter table transaction
    owner to postgres;

create table wallet
(
    amount     double precision,
    account_id bigint
        constraint fk4q1fs3jihuicq5afy27cy2gk9
            references account,
    id         bigserial
        primary key
);

alter table wallet
    owner to postgres;


