#!/usr/bin/env python3
"""Finalize the application info markdown with user-confirmed values."""
import re

with open("软件著作权申请资料/草稿/申请表信息.md", "r", encoding="utf-8") as f:
    content = f.read()

# Fill in confirmed fields
replacements = [
    (r"➤软件全称：.*", "➤软件全称：MindDream心理梦境分析平台"),
    (r"➤版本号：.*", "➤版本号：V1.0"),
    (r"➤著作权人：.*", "➤著作权人：许浩然"),
    (r"➤开发完成日期：.*", "➤开发完成日期：2026-02-24"),
    (r"➤首次发表日期：.*", "➤首次发表日期：未发表"),
    (r"➤开发该软件的操作系统：.*", "➤开发该软件的操作系统：Windows 11"),
    (r"➤软件开发环境 / 开发工具：.*", "➤软件开发环境 / 开发工具：Visual Studio Code"),
    (r"➤该软件的运行平台 / 操作系统：.*", "➤该软件的运行平台 / 操作系统：Windows 10/11"),
    (r"➤软件运行支撑环境 / 支持软件：.*", "➤软件运行支撑环境 / 支持软件：Node.js 16及以上、Java JDK 17及以上、MySQL 8.0及以上、Chrome/Edge浏览器"),
    (r"➤源程序量：.*", "➤源程序量：4749"),
    (r"➤页数：.*", "➤页数：96"),
]

for old, new in replacements:
    content = re.sub(old, new, content)

with open("软件著作权申请资料/草稿/申请表信息.md", "w", encoding="utf-8") as f:
    f.write(content)

print("OK - application info fields updated")
