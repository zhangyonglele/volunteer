# 数据库更改:

```
CREATE TABLE `enroll` (
  `main_id` int(6) unsigned NOT NULL COMMENT 'userInformationMainId',
  `real_name` varchar(30) NOT NULL COMMENT '姓名',
  `sex` char(6) NOT NULL COMMENT '性别',
  `tel_no` int(12) NOT NULL COMMENT '电话',
  `wechat` varchar(50) NOT NULL COMMENT '微信',
  `school` varchar(20) NOT NULL COMMENT '学校',
  `organization` varchar(20) NOT NULL COMMENT '组织',
  `introduction` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '自我简介',
  `first_choice` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第一志愿',
  `first_interview_score` char(2) DEFAULT NULL COMMENT '一面打分',
  `first_interview_impression` varchar(255) DEFAULT NULL COMMENT '一面印象',
  `second_choice` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '第二志愿',
  `second_interview_score` char(2) DEFAULT NULL COMMENT '二面打分',
  `second_interview_impression` varchar(255) DEFAULT NULL COMMENT '二面印象',
  `third_choice` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '第三志愿',
  `third_interview_score` char(2) DEFAULT NULL COMMENT '三面打分',
  `third_interview_impression` varchar(255) DEFAULT NULL COMMENT '三面印象',
  `final_department` varchar(10) DEFAULT NULL COMMENT '最终协调部门',
  `enroll_status` char(5) NOT NULL DEFAULT '0' COMMENT '0未面试1通过一面2通过所有面试3正在一面4正在二面5未通过面试',
  PRIMARY KEY (`main_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
```

```
create table enroll_pass
(
    pass_id     int(6) unsigned auto_increment
        primary key,
    main_id     int(6) unsigned  not null comment 'enroll_MainId',
    first_pass  int(5) default 0 null comment '第一志愿是否通过',
    second_pass int(5) default 0 null comment '第二志愿是否通过',
    third_pass  int(5) default 0 null comment '第三志愿是否通过'
);
```


```
CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `volunteer_test`.`cross_department` AS SELECT
	`enroll`.`main_id` AS `main_id`,
	`enroll`.`real_name` AS `real_name`,
	`enroll`.`sex` AS `sex`,
	`enroll`.`tel_no` AS `tel_no`,
	`enroll`.`wechat` AS `wechat`,
	`enroll`.`first_choice` AS `first_choice`,
	`enroll_pass`.`first_pass` AS `first_pass`,
	`enroll`.`second_choice` AS `second_choice`,
	`enroll_pass`.`second_pass` AS `second_pass`,
	`enroll`.`third_choice` AS `third_choice`,
	`enroll_pass`.`third_pass` AS `third_pass` 
FROM
	( `enroll` JOIN `enroll_pass` ) 
WHERE
	(
		( `enroll_pass`.`main_id` = `enroll`.`main_id` ) 
		AND ( `enroll`.`enroll_status` <> 502 ) 
		AND ( `enroll`.`enroll_status` <> 505 ) 
		AND ( isnull( `enroll`.`final_department` ) OR ( `enroll`.`final_department` = '' ) ) 
		AND (
			(
				(
				SELECT
					sum( ( ( `enroll_pass`.`first_pass` + `enroll_pass`.`second_pass` ) + `enroll_pass`.`third_pass` ) ) 
				FROM
					`enroll_pass` 
				WHERE
					( `enroll_pass`.`main_id` = `enroll`.`main_id` ) 
				) = 200 
			) 
			OR (
				(
				SELECT
					sum( ( ( `enroll_pass`.`first_pass` + `enroll_pass`.`second_pass` ) + `enroll_pass`.`third_pass` ) ) 
				FROM
					`enroll_pass` 
				WHERE
					( `enroll_pass`.`main_id` = `enroll`.`main_id` ) 
				) = 300 
			) 
			OR (
				(
				SELECT
					sum( ( ( `enroll_pass`.`first_pass` + `enroll_pass`.`second_pass` ) + `enroll_pass`.`third_pass` ) ) 
				FROM
					`enroll_pass` 
				WHERE
					( `enroll_pass`.`main_id` = `enroll`.`main_id` ) 
				) = 201 
			) 
			OR (
				(
				SELECT
					sum( ( ( `enroll_pass`.`first_pass` + `enroll_pass`.`second_pass` ) + `enroll_pass`.`third_pass` ) ) 
				FROM
					`enroll_pass` 
				WHERE
					( `enroll_pass`.`main_id` = `enroll`.`main_id` ) 
				) = 700 
			) 
		) 
	);
```

```
	CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `volunteer_test`.`pass_or_not` AS SELECT
    	`enroll`.`main_id` AS `main_id`,
    	`enroll`.`first_choice` AS `first_choice`,
    	`enroll_pass`.`first_pass` AS `first_pass`,
    	`enroll`.`second_choice` AS `second_choice`,
    	`enroll_pass`.`second_pass` AS `second_pass`,
    	`enroll`.`third_choice` AS `third_choice`,
    	`enroll_pass`.`third_pass` AS `third_pass`,
    	`enroll`.`final_department` AS `final_department`,
    	`enroll`.`enroll_status` AS `enroll_status` 
    FROM
    	( `enroll` JOIN `enroll_pass` ) 
    WHERE
    	( `enroll`.`main_id` = `enroll_pass`.`main_id` );
```

```
CREATE DEFINER= `root`@localhost  FUNCTION `departmenttotal` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
DECLARE res INT;
DECLARE res1 INT;
DECLARE res2 INT;
DECLARE res3 INT;
SET res = 0;
SET res1 = 0;
SET res2 = 0;
SET res3 = 0;
SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department;
SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department;
SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department;
SET res = res1 + res2;
SET res = res + res3;
RETURN res;
END
```



```
CREATE DEFINER= `root`@localhost  FUNCTION `crossdepartmenttotal` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
    DECLARE res INT;
    DECLARE res1 INT;
    DECLARE res2 INT;
    DECLARE res3 INT;
    SET res = 0;
    SET res1 = 0;
    SET res2 = 0;
    SET res3 = 0;
    SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department AND second_choice is not null and second_choice <>'';
    SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department;
    SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department;
    SET res = res1 + res2;
    SET res = res + res3;
    RETURN res;
END;
```


```
CREATE DEFINER= `root`@localhost  FUNCTION `departmentInterviewData` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
    DECLARE res INT;
DECLARE res1 INT;
DECLARE res2 INT;
DECLARE res3 INT;
SET res = 0;
SET res1 = 0;
SET res2 = 0;
SET res3 = 0;
SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department AND first_interview_score is not null and first_interview_score <>'' ;
SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department AND second_interview_score is not NULL and second_interview_score <>'';
SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department AND third_interview_score is not NULL and third_interview_score <>'';
SET res = res1 + res2;
SET res = res + res3;
RETURN res;
END;
```


```
CREATE DEFINER= `root`@localhost  FUNCTION `notDepartmentInterviewData` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
    DECLARE res INT;
    DECLARE res1 INT;
    DECLARE res2 INT;
    DECLARE res3 INT;
    SET res = 0;
    SET res1 = 0;
    SET res2 = 0;
    SET res3 = 0;
    SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department AND first_interview_score is null or first_interview_score = '';
    SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department AND second_interview_score is NULL or second_interview_score ='';
    SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department AND third_interview_score is NULL or second_interview_score ='';
    SET res = res1 + res2;
    SET res = res + res3;
    RETURN res;
END;
```

```
CREATE DEFINER= `root`@localhost  FUNCTION `departmentenrollbyman` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
    DECLARE res INT;
    DECLARE res1 INT;
    DECLARE res2 INT;
    DECLARE res3 INT;
    SET res = 0;
    SET res1 = 0;
    SET res2 = 0;
    SET res3 = 0;
    SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department AND sex='男';
    SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department AND sex='男';
    SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department AND sex='男';
    SET res = res1 + res2;
    SET res = res + res3;
    RETURN res;
END;
```

```
CREATE DEFINER= `root`@localhost  FUNCTION `departmentenrollbywoman` (department VARCHAR(15)) RETURNS INT(11)
BEGIN
    DECLARE res INT;
    DECLARE res1 INT;
    DECLARE res2 INT;
    DECLARE res3 INT;
    SET res = 0;
    SET res1 = 0;
    SET res2 = 0;
    SET res3 = 0;
    SELECT COUNT(first_choice) INTO res1 FROM enroll WHERE first_choice =  department AND sex='女';
    SELECT COUNT(second_choice) INTO res2 FROM enroll WHERE second_choice =  department AND sex='女';
    SELECT COUNT(third_choice) INTO res3 FROM enroll WHERE third_choice =  department AND sex='女';
    SET res = res1 + res2;
    SET res = res + res3;
    RETURN res;
END;
```

