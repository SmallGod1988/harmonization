#!/usr/bin/env python3
"""Сборка dist/index.html: шаблон src/app.html + данные data/data.json.

Использование:
    python3 scripts/build.py
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC, DATA, OUT = ROOT / "src" / "app.html", ROOT / "data" / "data.json", ROOT / "dist" / "index.html"

HEAD = ('<!doctype html>\n<html lang="ru">\n<head>\n<meta charset="utf-8">\n'
        '<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">\n'
        '</head>\n<body>\n')


def main():
    tpl = SRC.read_text(encoding="utf-8")
    if "__DATA__" not in tpl:
        raise SystemExit("В src/app.html нет метки __DATA__ — некуда подставить данные.")
    if not DATA.exists():
        raise SystemExit("Нет data/data.json. Сначала: python3 scripts/export_log.py путь/к/timelog_clean.xlsx")
    page = tpl.replace("__DATA__", DATA.read_text(encoding="utf-8"))
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(HEAD + page + "\n</body>\n</html>\n", encoding="utf-8")
    print(f"Готово: {OUT} ({OUT.stat().st_size // 1024} КБ)")


if __name__ == "__main__":
    main()
