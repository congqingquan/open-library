package org.cqq.openlibrary.common.component.checkfield;

/**
 * Flag 注解：比较时，仅会搜索标记了该注解的字段。若字段没有标记，会检查类上是否标记了该注解。注意：必须保证 source obj 的所属类使用了 Flag注解，否则会直接返回空结果。
 *
 * Ignore 注解：比较时，若在字段上发现了该注解，则会跳过匹配。同时，不会判断类上是否标记了该注解，因为 Ignore 注解写在类上无意义。
 *
 * AbstractModifiedFieldChecker.postProcess：可以继承后重写，对检查结果进行后置处理。
 */