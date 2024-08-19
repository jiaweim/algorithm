# 标准库

2024-01-30
***
## C 标准库

C 标准库操作字符数组 `char[]` 或 `const char*`。

参见：[fprintf](https://zh.cppreference.com/w/c/io/fprintf)、[fscanf](https://zh.cppreference.com/w/c/io/fscanf)、[空终止字节字符串](https://zh.cppreference.com/w/c/string/byte)：

- `printf("%s", s)`：用 `%s` 输出一个字符串（字符数组）。
- `scanf("%s", &s)`：用 `%s` 读入一个字符串（字符数组）。
- `sscanf(const char *__source, const char *__format, ...)`：从字符串 `__source` 读取变量，比如 `sscanf(str,"%d",&a)`。
- `sprintf(char *__stream, const char *__format, ...)`：将 `__format` 字符串里的内容输出到 `__stream` 中，比如 `sprintf(str,"%d",i)`。
strlen(const char *str)：返回从 str[0] 开始直到 '\0' 的字符数。注意，未开启 O2 优化时，该操作写在循环条件中复杂度是 \Theta(N) 的。
strcmp(const char *str1, const char *str2)：按照字典序比较 str1 str2 若 str1 字典序小返回负值，两者一样返回 0，str1 字典序更大则返回正值。请注意，不要简单的认为返回值只有 0、1、-1 三种，在不同平台下的返回值都遵循正负，但并非都是 0、1、-1。
strcpy(char *str, const char *src): 把 src 中的字符复制到 str 中，str src 均为字符数组头指针，返回值为 str 包含空终止符号 '\0'。
strncpy(char *str, const char *src, int cnt)：复制至多 cnt 个字符到 str 中，若 src 终止而数量未达 cnt 则写入空字符到 str 直至写入总共 cnt 个字符。
strcat(char *str1, const char *str2): 将 str2 接到 str1 的结尾，用 *str2 替换 str1 末尾的 '\0' 返回 str1。
strstr(char *str1, const char *str2)：若 str2 是 str1 的子串，则返回 str2 在 str1 的首次出现的地址；如果 str2 不是 str1 的子串，则返回 NULL。
strchr(const char *str, int c)：找到在字符串 str 中第一次出现字符 c 的位置，并返回这个位置的地址。如果未找到该字符则返回 NULL。
strrchr(const char *str, char c)：找到在字符串 str 中最后一次出现字符 c 的位置，并返回这个位置的地址。如果未找到该字符则返回 NULL。

## 参考

- https://oi-wiki.org/string/lib-func/