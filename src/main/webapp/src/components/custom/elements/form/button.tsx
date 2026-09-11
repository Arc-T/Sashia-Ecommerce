import { cn } from "@/lib/utils";
import { Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useTranslation } from "react-i18next";

interface RHFSubmitButtonProps {
  isSubmitting: boolean;
  loadingText?: string;
  defaultText?: string;
  disabled?: boolean;
  className?: string;
}

export function RHFSubmitButton({
  isSubmitting,
  loadingText = "sending",
  defaultText = "saving",
  disabled,
  className,
}: RHFSubmitButtonProps) {
  const { t } = useTranslation();
  return (
    <Button
      type="submit"
      disabled={disabled || isSubmitting}
      className={cn(
        "font-semibold flex items-center justify-center shadow-md transition-all hover:shadow-lg focus-visible:ring-2 focus-visible:ring-primary/50",
        isSubmitting && "opacity-60 cursor-not-allowed",
        className,
      )}
    >
      {isSubmitting ? t(loadingText) : t(defaultText)}
      {isSubmitting ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
    </Button>
  );
}
