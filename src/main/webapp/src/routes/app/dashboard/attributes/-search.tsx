import { useTranslation } from "react-i18next";
import { X } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  useSearchAttributeForm,
  type AttributeSearchFormValues,
} from "@/types/attribute";
import { RHFInput } from "@/components/custom/elements/form/input";
import { RHFSubmitButton } from "@/components/custom/elements/form/button";

interface Props {
  onSearch: (data: AttributeSearchFormValues) => void;
  onClear: () => void;
  isLoading?: boolean;
}

const AttributeSearch = ({ onSearch, onClear, isLoading }: Props) => {
  const { t } = useTranslation();
  const {
    control,
    handleSubmit,
    reset,
    formState: { isDirty, isValid, isSubmitting },
  } = useSearchAttributeForm();

  const handleClear = () => {
    reset();
    onClear();
  };

  return (
    <form onSubmit={handleSubmit(onSearch)} className="w-full">
      {/* Search Input - Always visible */}
      <div className="flex items-center gap-2 p-4">
        <div className="flex-1 max-w-sm">
          <RHFInput
            name="name"
            control={control}
            label="attribute_name"
            placeholder="search_attributes"
          />
        </div>
      </div>

      <div
        className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 p-4
        transition-all duration-200 overflow-hidden border-t border-border/50
        max-h-96"
      >
        <p className="text-xs text-muted-foreground flex-1">
          {t("search_hint")}
        </p>

        <div className="flex flex-col sm:flex-row gap-2 w-full sm:w-auto">
          {isDirty && (
            <Button
              type="button"
              variant="outline"
              onClick={handleClear}
              className="w-full sm:w-auto font-semibold"
            >
              {t("clear_all")}
              <X className="h-4 w-4 ml-1" />
            </Button>
          )}
          <RHFSubmitButton
            className="w-full sm:w-auto"
            isSubmitting={isSubmitting}
            disabled={!isValid || isLoading}
            defaultText="search"
            loadingText="searching"
          />
        </div>
      </div>
    </form>
  );
};

export default AttributeSearch;
