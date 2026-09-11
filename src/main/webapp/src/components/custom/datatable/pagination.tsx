import { ChevronLeft, ChevronRight } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface PaginationProps {
  page: number;
  pageSize: number;
  totalItems: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (pageSize: number) => void;
}

export default function Pagination({
  page,
  pageSize,
  totalItems,
  onPageChange,
  onPageSizeChange,
}: PaginationProps) {
  const { t, i18n } = useTranslation();
  const isRTL = i18n.dir() === "rtl";
  const totalPages = Math.ceil(totalItems / pageSize);

  return (
    <div className="flex flex-wrap p-4 items-center justify-between gap-3 text-sm text-slate-500 dark:text-slate-400 border-t border-border mt-4">
      <div className="flex items-center gap-2">
        <span>{t("rows_per_page")}</span>
        <Select
          value={String(pageSize)}
          onValueChange={(v) => {
            onPageSizeChange(Number(v));
          }}
        >
          <SelectTrigger className="w-20 h-8">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            {[5, 10, 20, 50].map((n) => (
              <SelectItem key={n} value={String(n)}>
                {n}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
      <div className="flex items-center gap-1">
        <span>
          {t("showing")} {page * pageSize + 1}-
          {Math.min((page + 1) * pageSize, totalItems)} {t("of")} {totalItems}{" "}
          {t("results")}
        </span>
      </div>
      <div className="flex items-center gap-1">
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          disabled={page === 0}
          onClick={() => onPageChange(page - 1)}
        >
          {isRTL ? (
            <ChevronRight className="w-4 h-4" />
          ) : (
            <ChevronLeft className="w-4 h-4" />
          )}
        </Button>
        {Array.from({ length: Math.min(totalPages, 5) }, (_, i) => {
          const pageNum =
            totalPages <= 5
              ? i
              : Math.max(0, Math.min(page - 2, totalPages - 5)) + i;
          return (
            <Button
              key={pageNum}
              variant={page === pageNum ? "default" : "outline"}
              size="icon"
              className={'h-8 w-8'}
              onClick={() => onPageChange(pageNum)}
            >
              {pageNum + 1}
            </Button>
          );
        })}
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          disabled={page >= totalPages - 1}
          onClick={() => onPageChange(page + 1)}
        >
          {isRTL ? (
            <ChevronLeft className="w-4 h-4" />
          ) : (
            <ChevronRight className="w-4 h-4" />
          )}
        </Button>
      </div>
    </div>
  );
}
