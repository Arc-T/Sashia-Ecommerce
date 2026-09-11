import { Input } from "@/components/ui/input";
import { t } from "i18next";
import { Button } from "@/components/ui/button";
import { Search, X } from "lucide-react";
import {
  useSearchAttributeForm,
  type AttributeSearchFormValues,
} from "@/types/attribute";
import { useState } from "react";

interface AttributeSearchProps {
  onSearch: (data: AttributeSearchFormValues) => void;
  onClear: () => void;
  isLoading?: boolean;
  initialValues?: Partial<AttributeSearchFormValues>;
}

const AttributeSearch = ({
  onSearch,
  onClear,
  isLoading = false,
}: AttributeSearchProps) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { isDirty, isValid },
  } = useSearchAttributeForm();

  const [isExpanded, setIsExpanded] = useState(false);

  const onSubmit = (data: AttributeSearchFormValues) => {
    onSearch(data);
  };

  const handleClear = () => {
    reset();
    onClear();
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="w-full transition-all duration-200"
    >
      {/* Header - always visible */}
      <div className="flex items-center justify-between gap-2 p-2 sm:p-4">
        <div className="relative flex-1 max-w-sm">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            type="text"
            placeholder={t("search_attributes") || t("attribute_name")}
            className="pl-9 bg-background focus:ring-2 focus:ring-primary/20 transition-all"
            {...register("name")}
            aria-label={t("attribute_name")}
          />
        </div>

        <div className="flex items-center gap-2">
          <Button
            type="submit"
            size="default"
            disabled={isLoading || !isValid}
            className="gap-2"
          >
            {isLoading ? (
              <div className="h-4 w-4 animate-spin rounded-full border-2 border-background border-t-transparent" />
            ) : (
              <Search className="h-4 w-4" />
            )}
            <span className="hidden sm:inline">{t("search")}</span>
          </Button>

          {isDirty && (
            <Button
              type="button"
              variant="ghost"
              size="default"
              onClick={handleClear}
              className="gap-2 text-muted-foreground hover:text-destructive"
              aria-label={t("clear")}
            >
              <X className="h-4 w-4" />
              <span className="hidden sm:inline">{t("clear")}</span>
            </Button>
          )}

          <Button
            type="button"
            variant="ghost"
            size="sm"
            onClick={() => setIsExpanded(!isExpanded)}
            className="lg:hidden"
            aria-label={t("more_filters")}
          >
            <span className="text-sm">{isExpanded ? "−" : "+"}</span>
          </Button>
        </div>
      </div>

      {/* Advanced filters - collapsible on mobile */}
      <div
        className={`
          overflow-hidden transition-all duration-200
          ${isExpanded ? "max-h-96" : "max-h-0 lg:max-h-96"}
          border-t border-border/50
        `}
      >
        <div className="grid gap-3 p-3 sm:p-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {/* Add more filter fields here as needed */}
          {/* Example:
          <div className="space-y-1.5">
            <Label htmlFor="type">{t("attribute_type")}</Label>
            <Select>
              <SelectTrigger id="type">
                <SelectValue placeholder={t("select_type")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="text">Text</SelectItem>
                <SelectItem value="number">Number</SelectItem>
              </SelectContent>
            </Select>
          </div>
          */}

          <div className="text-xs text-muted-foreground col-span-full">
            {t("search_hint") || t("type_to_search_attributes")}
          </div>
        </div>
      </div>

      {/* Active filters summary - shows when filters are applied */}
      {isDirty && (
        <div className="flex flex-wrap gap-1.5 p-3 pt-0 text-xs text-muted-foreground border-t border-border/50">
          <span className="font-medium">{t("active_filters")}:</span>
          <Button
            variant="link"
            size="sm"
            className="h-auto p-0 text-xs text-primary"
            onClick={handleClear}
          >
            {t("clear_all")}
          </Button>
        </div>
      )}
    </form>
  );
};

export default AttributeSearch;
