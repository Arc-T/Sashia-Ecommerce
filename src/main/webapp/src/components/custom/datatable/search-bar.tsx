import { useState, type ReactNode } from "react";
import { RefreshCw, X, Clock, Search } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useTranslation } from "react-i18next";

interface Props {
  children?: ReactNode;
  activeFilterCount?: number;
  onClearFilters?: () => void;
  actions?: ReactNode;
  handleRefresh: () => void;
  isRefreshing: boolean;
}

export default function ActionBar({
  children,
  activeFilterCount = 0,
  onClearFilters,
  actions,
  handleRefresh,
  isRefreshing,
}: Props) {
  const [showFilters, setShowFilters] = useState(false);
  const { t } = useTranslation();

  const formattedDate = new Date("2026-06-27T14:30:00").toString();

  return (
    <div className="space-y-4 py-4">
      <div className="flex items-center justify-between gap-2 flex-wrap border-b border-border pb-4">
        <div className="flex items-center gap-2 flex-wrap px-4">
          <Button
            onClick={() => setShowFilters(!showFilters)}
            variant={showFilters ? "default" : "outline"}
            size="sm"
            className="relative gap-1.5"
          >
            <Search className="w-4 h-4" />
            {t("search")}
            {activeFilterCount > 0 && (
              <span className="ml-0.5 px-1.5 py-0.5 text-xs bg-primary-foreground text-primary rounded-full">
                {activeFilterCount}
              </span>
            )}
          </Button>

          <Button
            variant="outline"
            size="sm"
            onClick={handleRefresh}
            disabled={isRefreshing}
            className="gap-1.5"
          >
            <RefreshCw
              className={`w-4 h-4 ${isRefreshing ? "animate-spin" : ""}`}
            />
            {t("refresh")}
          </Button>

          {onClearFilters && activeFilterCount > 0 && (
            <Button
              variant="ghost"
              size="sm"
              onClick={onClearFilters}
              className="gap-1.5 text-muted-foreground"
            >
              <X className="w-4 h-4" />
              Clear
            </Button>
          )}
        </div>

        <div className="flex items-center gap-3 px-4">
          <div className="flex gap-1.5 text-xs text-muted-foreground whitespace-nowrap">
            <Clock className="w-3.5 h-3.5" />
            <span>Updated: {formattedDate}</span>
          </div>
          {actions}
        </div>
      </div>

      {showFilters && children && (
        <div className="mx-4 rounded-lg border bg-muted animate-in fade-in slide-in-from-top-2 duration-200">
          <div className="flex flex-wrap items-center">{children}</div>
        </div>
      )}
    </div>
  );
}
